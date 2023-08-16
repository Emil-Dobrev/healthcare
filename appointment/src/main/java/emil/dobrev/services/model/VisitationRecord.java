package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "visitation_records")
public class VisitationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime visitDateTime;
    @Column(nullable = false)
    private Long appointmentId;
    @Column(nullable = false)
    private Long patientId;
    private Long doctorId;
    private String diagnosis;
    private String treatmentPlan;
}

