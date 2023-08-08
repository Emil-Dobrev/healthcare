package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;

import java.time.LocalDate;

public interface AppointmentService {
    AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest, Long patientId, String roles);

    void getAllAvailableSlots(Long doctorId, String roles, LocalDate requestedDate);
}
