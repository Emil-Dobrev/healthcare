package emil.dobrev.services.entity;

import emil.dobrev.services.enums.DoctorSpecialization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "doctors")
public class Doctor  extends User{

    @Enumerated(EnumType.STRING)
    @Column
    DoctorSpecialization specialization;
    @Column
    private String phoneNumber;
}
