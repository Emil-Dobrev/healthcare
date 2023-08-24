package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private int frequencyPerDay; // e.g., "Once daily", "Twice daily"
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private int durationInHoursBetweenDoses; // Duration in hours between consecutive doses
    private LocalDateTime firstDosage;
    private LocalDateTime timeForNextDosage;
    private int dosageTakenToday = 0;
    private LocalDateTime timeForLastDosageNotificationSend;
    @Column(nullable = false)
    private boolean isActive = false;
}
