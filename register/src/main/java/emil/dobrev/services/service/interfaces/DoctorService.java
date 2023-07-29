package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.RegistrationResponse;

public interface DoctorService {
    
    RegistrationResponse register(DoctorRegistrationRequest doctorRegistrationRequest);

    DoctorDTO getDoctorById(Long id);
}
