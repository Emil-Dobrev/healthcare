package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateMedicationScheduleRequest;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static emil.dobrev.services.constant.Constants.PATIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationScheduleServiceImpTest {

    @Mock
    private MedicationScheduleRepository medicationScheduleRepository;
    @Mock
    private KafkaService kafkaService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private MedicationScheduleServiceImp medicationScheduleServiceImp;
    private String roles;

    @BeforeEach
    void setUp() {
        roles = PATIENT;
    }

    @Test
    void shouldCreateMedicationSchedule() {
        long userId = 1l;
        LocalDate now = LocalDate.now();

        CreateMedicationScheduleRequest request = CreateMedicationScheduleRequest.builder()
                .name("test medication")
                .endDate(now.plusDays(2))
                .frequency(1)
                .startDate(now)
                .durationBetweenDoses(20)
                .dosage(4.2)
                .dosageUnit("ml")
                .userId(userId)
                .timeFirstDosageTaken(LocalDateTime.of(2023, 8, 15, 9, 30)).build();

        medicationScheduleServiceImp.createMedicationSchedule(userId, roles, request);
        verify(medicationScheduleRepository, times(1)).save(any(MedicationSchedule.class));
    }

    @Test
    void shouldGetAllMedicationSchedulesForUser() {
        long userId = 1l;

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .userId(1l)
                .build();

        MedicationSchedule medicationSchedule2 = MedicationSchedule.builder()
                .userId(1l)
                .build();

        when(medicationScheduleRepository.findAllByUserId(userId))
                .thenReturn(Optional.of(List.of(medicationSchedule2, medicationSchedule)));

        var medicationsSchedules = medicationScheduleServiceImp.getAllMedicationScheduleForUser(userId, roles);

        verify(medicationScheduleRepository, times(1)).findAllByUserId(userId);

        assertNotNull(medicationsSchedules);
        assertEquals(medicationsSchedules.size(), 2);
    }

    @Test
    void shouldSetTimeFirstDosageTaken() {
        long userId = 1l;
        long medicationScheduleId = 5l;
        LocalDateTime timeFirstDosageTaken = LocalDateTime.of(2023, 8, 15, 9, 30);

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .userId(userId)
                .build();

        when(medicationScheduleRepository.findById(medicationScheduleId))
                .thenReturn(Optional.of(medicationSchedule));

        medicationScheduleServiceImp
                .setTimeFirstDosageTaken(userId, roles, medicationScheduleId, timeFirstDosageTaken);

        verify(medicationScheduleRepository, times(1)).findById(medicationScheduleId);
        verify(medicationScheduleRepository, times(1)).save(any(MedicationSchedule.class));
    }

    @Test
    void shouldThrowUnauthorizedWhenTryToSetFirstTDosageTakenDifferentUser() {
        long userId = 1l;
        long medicationScheduleId = 5l;

        LocalDateTime timeFirstDosageTaken = LocalDateTime.of(2023, 8, 15, 9, 30);

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .userId(2L)
                .build();

        when(medicationScheduleRepository.findById(medicationScheduleId))
                .thenReturn(Optional.of(medicationSchedule));

        assertThrows(UnauthorizedException.class, () -> {
            medicationScheduleServiceImp
                    .setTimeFirstDosageTaken(userId, roles, medicationScheduleId, timeFirstDosageTaken);
        });
    }
}