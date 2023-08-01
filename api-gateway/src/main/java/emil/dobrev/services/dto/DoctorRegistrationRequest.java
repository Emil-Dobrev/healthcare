package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emil.dobrev.services.enums.DoctorSpecialization;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DoctorRegistrationRequest(
        @Email(message = "Invalid email!")
        String email,
        @NotBlank(message = "Password  can't be blank")
        String password,
        @NotBlank(message = "First name can't be blank")
        String firstName,
        @NotBlank(message = "Last name can't be blank")
        String lastName,
        @NonNull
        @Enumerated(EnumType.STRING)
        DoctorSpecialization specialization,
        @NotBlank(message = "Phone number can't be blank")
        String phoneNumber
) {
}
