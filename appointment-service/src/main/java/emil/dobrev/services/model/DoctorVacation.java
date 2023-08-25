package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctor_vacation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorVacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private DoctorSchedule doctor;

    @ElementCollection
    @CollectionTable(name = "doctor_vacation_dates", joinColumns = @JoinColumn(name = "vacation_id"))
    @Column(name = "vacation_date")
    private List<LocalDate> vacationDate;

}

