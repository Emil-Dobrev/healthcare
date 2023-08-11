package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.dto.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest, Long patientId, String roles);

    List<TimeSlot> getAllAvailableSlots(Long doctorId, String roles, LocalDate requestedDate);
}
