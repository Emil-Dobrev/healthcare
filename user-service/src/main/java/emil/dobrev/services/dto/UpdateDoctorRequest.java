package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.model.Doctor;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateDoctorRequest(@NonNull String firstName,
                                  @NonNull String lastName,
                                  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String password,
                                  @NonNull @DateTimeFormat LocalDate birthdate,
                                  @NonNull
                                  @Enumerated(EnumType.STRING)
                                  DoctorSpecialization doctorSpecialization,
                                  @NonNull
                                  String phoneNumber

) {
    public UpdateDoctorRequest(Doctor doctor) {
        this(   doctor.getFirstName(),
                doctor.getLastName(),
                null,
                doctor.getBirthdate(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber()
        );
    }

}
