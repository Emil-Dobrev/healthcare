package emil.dobrev.services.service;

import emil.dobrev.services.dto.VisitationRequest;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.model.VisitationRecord;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.VisitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitationServiceImpTest {
    @Mock
    private VisitationRepository visitationRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private VisitationServiceImp visitationService;

    private Appointment appointment;
    private String roles;
    private Long doctorId;

    @BeforeEach
    void setUp() {
        this.appointment = Appointment.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(23L)
                .appointmentDateTime(LocalDateTime.of(2023, 8, 15, 15, 30))
                .endOFAppointmentDateTime(LocalDateTime.of(2023, 8, 15, 16, 0))
                .build();

        this.doctorId = 1L;
        this.roles = "ROLE_DOCTOR";
    }

    @Test
    void shouldAddVisitation() {

        var appointmentId = 1L;

        var request = new VisitationRequest(
                1L,
                "test",
                "test"
        );

        var response = VisitationRecord.builder()
                .appointmentId(appointment.getId())
                .visitDateTime(appointment.getAppointmentDateTime())
                .diagnosis(request.diagnosis())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .treatmentPlan(request.treatmentPlan())
                .build();

        when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.ofNullable(appointment));

        visitationService.addVisitation(request, doctorId, roles);

        verify(visitationRepository).save(response);
    }

    @Test
    void shouldThrowUnauthorizedForGetAllVisitationsForPatient() {
        assertThrows(UnauthorizedException.class, () -> {
            visitationService.getAllVisitationsForPatient(1L,"role", 2L);
        });
    }
}