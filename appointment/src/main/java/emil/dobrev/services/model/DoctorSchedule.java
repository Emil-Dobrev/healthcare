package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor_schedules", uniqueConstraints = @UniqueConstraint(columnNames = "doctorId"))
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
    private List<DayOfWeek> workingDays = new ArrayList<>();

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalTime breakFrom;

    @Column(nullable = false)
    private LocalTime breakTo;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorHoliday> holiday = new ArrayList<>();

}
