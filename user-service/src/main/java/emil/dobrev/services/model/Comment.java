package emil.dobrev.services.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    @Id

    @GenericGenerator(
            name = "comment",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "comment"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")}
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @ManyToOne
    private Doctor doctor;
    private String createdBy;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @NotBlank
    private String description;
}
