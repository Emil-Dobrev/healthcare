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
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(nullable = false)
    private LocalDateTime endOFAppointmentDateTime;

    @Column(nullable = false)
    private Long doctorId; // Reference ID to the Doctor entity

    @Column(nullable = false)
    private Long patientId; // Reference ID to the Patient entity

}