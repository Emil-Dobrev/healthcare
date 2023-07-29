package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.dto.RegistrationResponse;

public interface PatientService {

    RegistrationResponse register(PatientRegistrationRequest patientRegistrationRequest);
}
