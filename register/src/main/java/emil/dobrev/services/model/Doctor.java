package emil.dobrev.services.model;

import emil.dobrev.services.enums.DoctorSpecialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "doctors")
public class Doctor extends User {

    @Enumerated(EnumType.STRING)
    @Column
    private DoctorSpecialization specialization;
    @Column
    private String phoneNumber;
}
