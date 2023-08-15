package emil.dobrev.services.service;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.dto.Holiday;
import emil.dobrev.services.dto.TimeSlot;
import emil.dobrev.services.exception.NotValidWorkingDayException;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImpTest {

    @InjectMocks
    private AppointmentServiceImp appointmentServiceImp;

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;
    @Mock
    private KafkaService kafkaService;
    private DoctorSchedule doctorSchedule;
    private String roles;

    @Test
    void create() {
    }

    @BeforeEach
    void setUp() {
        this.roles = "ROLE_DOCTOR";
        doctorSchedule = new DoctorSchedule();
        doctorSchedule.setStartTime(LocalTime.of(9, 0));
        doctorSchedule.setEndTime(LocalTime.of(17, 0));
        doctorSchedule.setBreakFrom(LocalTime.of(12, 0));
        doctorSchedule.setBreakTo(LocalTime.of(13, 0));
        doctorSchedule.setWorkingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        Long doctorId = 1L;
        long patientId = 1L;
        String role = "ROLE_PATIENT";
        var request = new CreateAppointmentRequest(
                LocalDateTime.of(2023, 8, 15, 15, 30),
                doctorId
        );

        when(doctorScheduleRepository.findByDoctorId(doctorId))
                .thenReturn(Optional.of(doctorSchedule));

        when(appointmentRepository
                .findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual(
                        doctorId,
                        request.appointmentDateTime(),
                        request.appointmentDateTime().plusMinutes(30)
                )).thenReturn(Collections.emptyList());

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setAppointmentDateTime(request.appointmentDateTime());
        savedAppointment.setEndOFAppointmentDateTime(request.appointmentDateTime().plusMinutes(30));
        savedAppointment.setDoctorId(doctorId);
        savedAppointment.setPatientId(patientId);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // Call the service method
        AppointmentResponse response = appointmentServiceImp.create(request, patientId, role);


        assertNotNull(response);
        assertEquals(request.appointmentDateTime(), response.appointmentDateTime());
        assertEquals(request.appointmentDateTime().plusMinutes(30), response.appointmentDateTime().plusMinutes(30));


        verify(appointmentRepository).findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual(
                doctorId,
                request.appointmentDateTime(),
                request.appointmentDateTime().plusMinutes(30)
        );
        verify(appointmentRepository).save(any(Appointment.class));
        verify(kafkaService).sendAppointmentNotification(savedAppointment);
    }

    @Test
    void shouldGetAllAvailableSlots() {
        Long doctorId = 1L;
        LocalDate requestedDate = LocalDate.of(2023, 8, 14);

        // Mock doctor schedule
        DoctorSchedule doctorSchedule = this.doctorSchedule;

        // Mock holidays
        List<Holiday> holidays = Collections.emptyList();

        when(doctorScheduleRepository.findByDoctorId(doctorId))
                .thenReturn(Optional.of(doctorSchedule));
        when(doctorScheduleRepository.getAllHolidaysForDoctor(doctorSchedule.getId()))
                .thenReturn(Optional.of(holidays));

        Appointment appointment = Appointment.builder()
                .doctorId(1L)
                .patientId(23L)
                .appointmentDateTime(LocalDateTime.of(2023, 8, 15, 15, 30))
                .endOFAppointmentDateTime(LocalDateTime.of(2023, 8, 15, 16, 0))
                .build();

        Appointment appointment2 = Appointment.builder()
                .doctorId(1L)
                .patientId(23L)
                .appointmentDateTime(LocalDateTime.of(2023, 8, 15, 16, 30))
                .endOFAppointmentDateTime(LocalDateTime.of(2023, 8, 15, 17, 0))
                .build();

        // Mock appointments
        List<Appointment> appointments = List.of(appointment, appointment2);
        when(appointmentRepository.findAppointmentsByDoctorIdAndAppointmentDateTimeBetween(
                doctorId,
                LocalDateTime.of(requestedDate, doctorSchedule.getStartTime()),
                LocalDateTime.of(requestedDate, doctorSchedule.getEndTime())
        )).thenReturn(appointments);

        // Call the method to be tested
        List<TimeSlot> availableSlots = appointmentServiceImp.getAllAvailableSlots(doctorId, roles, requestedDate);

        // Assert
        // You can add more assertions here based on the expected behavior
        assertNotNull(availableSlots);
        assertEquals(12, availableSlots.size());
        // Verify other conditions based on the test scenario
    }

    @Test
    void shouldThrowNotValidWorkingDayException() {
        Long doctorId = 1L;
        String roles = "ROLE_DOCTOR";
        LocalDate requestedDate = LocalDate.of(2023, 8, 17);

        // Mock doctor schedule
        DoctorSchedule doctorSchedule = this.doctorSchedule;
        List<Holiday> holidays = Collections.emptyList();


        when(doctorScheduleRepository.findByDoctorId(doctorId))
                .thenReturn(Optional.of(doctorSchedule));

        when(doctorScheduleRepository.getAllHolidaysForDoctor(doctorSchedule.getId()))
                .thenReturn(Optional.of(holidays));

        Appointment appointment = Appointment.builder()
                .doctorId(1L)
                .patientId(23L)
                .appointmentDateTime(LocalDateTime.of(2023, 8, 15, 15, 30))
                .endOFAppointmentDateTime(LocalDateTime.of(2023, 8, 15, 16, 0))
                .build();


        assertThrows(NotValidWorkingDayException.class, () -> {
            appointmentServiceImp.getAllAvailableSlots(doctorId, roles, requestedDate);
        });

    }
}