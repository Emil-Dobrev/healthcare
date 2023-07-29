package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface PatientService {

    AuthenticationResponse register(PatientRegistrationRequest patientRegistrationRequest);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
