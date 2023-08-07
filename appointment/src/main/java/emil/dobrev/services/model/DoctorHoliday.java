package emil.dobrev.services.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctor_holidays")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private DoctorSchedule doctor;

    @ElementCollection
    @CollectionTable(name = "doctor_holiday_dates", joinColumns = @JoinColumn(name = "holiday_id"))
    @Column(name = "holiday_date")
    private List<LocalDate> holidayDate;

}

