package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.CreateAppointmentRequest;

public interface AppointmentService {
    void create(CreateAppointmentRequest createAppointmentRequest);
}
