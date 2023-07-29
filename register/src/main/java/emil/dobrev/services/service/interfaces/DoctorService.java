package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DoctorService  {
    
    AuthenticationResponse register(DoctorRegistrationRequest doctorRegistrationRequest);

    DoctorDTO getDoctorById(Long id);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
