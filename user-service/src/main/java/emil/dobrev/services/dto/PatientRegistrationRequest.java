package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public record PatientRegistrationRequest(
        @Email(message = "Invalid email!")
        String email,
        @NotBlank(message = "Password  can't be blank")
        String password,
        @NotBlank(message = "First name can't be blank")
        String firstName,
        @NotBlank(message = "Last name can't be blank")
        String lastName,
        @NonNull
        @DateTimeFormat
        Date birthdate,
        @NotBlank(message = "Phone number can't be blank")
        String phoneNumber
) {
}
