package emil.dobrev.services.dto;

import emil.dobrev.services.enums.DoctorSpecialization;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    @NotBlank
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private DoctorSpecialization specialization;
    @NotBlank
    private String phoneNumber;
}
