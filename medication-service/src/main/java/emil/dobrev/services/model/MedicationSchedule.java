package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "medication_schedule")
public class MedicationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double dosage;
    @Column(nullable = false)
    private String dosageUnit; // e.g., mg, mL
    @Column(nullable = false)
    private int frequency; // e.g., "Once daily", "Twice daily"
    @Column(nullable = false)
    private String startDate;
    @Column(nullable = false)
    private String endDate;

    @Column(name = "daysOfWeek", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek; // List of days when the medication should be taken
    @Column(nullable = false)
    private int durationBetweenDoses; // Duration in hours between consecutive doses
    @Column(nullable = false)
    private boolean isActive = true;
}
