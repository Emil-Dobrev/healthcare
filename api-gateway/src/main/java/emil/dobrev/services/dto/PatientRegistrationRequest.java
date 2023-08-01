package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
public record PatientRegistrationRequest(
        @Email(message = "Invalid email!")
        String email,
        @NotBlank(message = "Password  can't be blank")
        String password,
        @NotBlank(message = "First name can't be blank")
        String firstName,
        @NotBlank(message = "Last name can't be blank")
        String lastName
) {
}
