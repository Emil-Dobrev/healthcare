package emil.dobrev.services.job;

import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import emil.dobrev.services.service.KafkaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendRemainderForNextDosageJobTest {
    @Mock
    private MedicationScheduleRepository medicationScheduleRepository;
    @Mock
    private KafkaService kafkaService;
    @InjectMocks
    private SendRemainderForNextDosageJob sendRemainderForNextDosageJob;

    @Test
    void sendRemainderForNextDosageJob() {

        LocalDate now = LocalDate.now();

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .id(1L)
                .name("test medication")
                .endDate(now.plusDays(2))
                .frequencyPerDay(2)
                .startDate(now)
                .durationInHoursBetweenDoses(12)
                .dosage(4.2)
                .dosageUnit("ml")
                .userId(1L)
                .isActive(true)
                .firstDosage(LocalDateTime.of(2023, 8, 15, 9, 30))
                .build();

        MedicationSchedule medicationSchedule2= MedicationSchedule.builder()
                .id(2L)
                .name("test medication")
                .endDate(now.plusDays(2))
                .frequencyPerDay(1)
                .startDate(now)
                .durationInHoursBetweenDoses(20)
                .dosage(4.2)
                .dosageUnit("ml")
                .userId(1L)
                .isActive(true)
                .firstDosage(LocalDateTime.of(2023, 8, 15, 9, 30))
                .timeForNextDosage(LocalDateTime.of(2023, 8, 15, 9, 30)).build();


        MedicationSchedule medicationSchedule3= MedicationSchedule.builder()
                .id(2L)
                .name("test medication")
                .endDate(now.plusDays(2))
                .frequencyPerDay(1)
                .startDate(now)
                .durationInHoursBetweenDoses(20)
                .dosage(4.2)
                .dosageUnit("ml")
                .dosageTakenToday(2)
                .userId(1L)
                .isActive(true)
                .firstDosage(LocalDateTime.of(2023, 8, 15, 9, 30))
                .timeForNextDosage(LocalDateTime.of(2023, 8, 15, 9, 30)).build();


        when(medicationScheduleRepository.findAllByIsActive(true))
                .thenReturn(List.of(medicationSchedule,medicationSchedule2));

        sendRemainderForNextDosageJob.SendRemainderForNextDosageJob();

        verify(medicationScheduleRepository, times(1)).findAllByIsActive(true);
        verify(medicationScheduleRepository, times(2)).save(any(MedicationSchedule.class));
        verify(kafkaService, times(2)).sendMedicationNotification(any());
    }
}