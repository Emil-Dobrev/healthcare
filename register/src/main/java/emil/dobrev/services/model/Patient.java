package emil.dobrev.services.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "patients")
public class Patient extends User{
}
