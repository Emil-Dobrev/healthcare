package emil.dobrev.services.service;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.ScheduleRequest;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleServiceImpTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorScheduleServiceImp doctorScheduleService;

    private String roles;
    private DoctorSchedule doctorSchedule;

    @BeforeEach
    void setUp() {
        this.roles = "ROLE_DOCTOR";
    }

    @Test
    void shouldCreateSchedule() {
        Long doctorId = 1L;
        ScheduleRequest request = new ScheduleRequest(
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );

        // Call the method to be tested
        doctorScheduleService.createSchedule(doctorId, roles, request);


        // Verify that the save method was called with the correct DoctorSchedule object
        verify(doctorScheduleRepository).save(argThat(savedSchedule ->
                savedSchedule.getDoctorId().equals(doctorId) &&
                        savedSchedule.getWorkingDays().equals(request.workingDays()) &&
                        savedSchedule.getStartTime().equals(request.startTime()) &&
                        savedSchedule.getEndTime().equals(request.endTime()) &&
                        savedSchedule.getBreakFrom().equals(request.breakFrom()) &&
                        savedSchedule.getBreakTo().equals(request.breakTo())
        ));
    }

    @Test
    void shouldUpdateSchedule() {
        var scheduleId = 1L;
        var doctorId = 1L;
        doctorSchedule = new DoctorSchedule();
        doctorSchedule.setStartTime(LocalTime.of(9, 0));
        doctorSchedule.setEndTime(LocalTime.of(17, 0));
        doctorSchedule.setBreakFrom(LocalTime.of(12, 0));
        doctorSchedule.setBreakTo(LocalTime.of(13, 0));
        doctorSchedule.setWorkingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
        doctorSchedule.setDoctorId(1L);

        ScheduleRequest request = new ScheduleRequest(
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                LocalTime.of(10, 0),
                LocalTime.of(15, 0),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0)
        );

        when(doctorScheduleRepository.findById(scheduleId))
                .thenReturn(Optional.of(doctorSchedule));

        when(doctorScheduleRepository.updateSchedule(
                scheduleId,
                request.workingDays(),
                request.startTime(),
                request.endTime(),
                request.breakFrom(),
                request.breakTo()
        )).thenReturn(DoctorScheduleDTO.builder()
                .startTime(request.startTime())
                .endTime(request.endTime())
                .workingDays(request.workingDays())
                .build());

        DoctorScheduleDTO doctorScheduleDTO = doctorScheduleService
                .updateSchedule(doctorId, roles, scheduleId, request);

        verify(doctorScheduleRepository).updateSchedule(
                scheduleId,
                request.workingDays(),
                request.startTime(),
                request.endTime(),
                request.breakFrom(),
                request.breakTo()
        );

        // Verify that the returned DoctorScheduleDTO matches the expected values
        assertEquals(request.workingDays(), doctorScheduleDTO.getWorkingDays());
        assertEquals(request.startTime(), doctorScheduleDTO.getStartTime());
        assertEquals(request.endTime(), doctorScheduleDTO.getEndTime());
    }


}