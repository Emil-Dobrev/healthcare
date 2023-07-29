package emil.dobrev.services.model;

import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

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
