package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor_schedules")
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "workingDays",
            columnDefinition = "text[]"
    )
    private List<DayOfWeek> workingDays;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalTime breakFrom;

    @Column(nullable = false)
    private LocalTime breakTo;

    @ElementCollection
    @CollectionTable(name = "doctor_schedule_holidays", joinColumns = @JoinColumn(name = "doctor_schedule_id"))
    @Column(name = "holiday")
    private List<LocalDate> holiday;

}
