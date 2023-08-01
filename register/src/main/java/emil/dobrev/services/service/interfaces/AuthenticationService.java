package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse register(DoctorRegistrationRequest doctorRegistrationRequest);

    AuthenticationResponse register(PatientRegistrationRequest patientRegistrationRequest);

//    AuthenticationResponse authenticate(AuthenticationRequest request);


}
