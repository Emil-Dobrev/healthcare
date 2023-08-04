package emil.dobrev.services.model;

import emil.dobrev.services.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
public abstract class User{

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    protected Long id;
    @Column(nullable = false, unique = true)
    protected String email;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false)
    @NotBlank
    protected String firstName;
    @Column(nullable = false)
    @NotBlank
    protected String lastName;

    @Enumerated(EnumType.STRING)
    protected List<Role> roles;
}
