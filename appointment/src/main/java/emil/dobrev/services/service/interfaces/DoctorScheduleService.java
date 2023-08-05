package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.CreateScheduleRequest;
import emil.dobrev.services.dto.DoctorScheduleDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DoctorScheduleService {
    void createSchedule(Long id, String roles, CreateScheduleRequest request);

    DoctorScheduleDTO getSchedule(Long id);

    void setHoliday(Long id, String roles, List<LocalDate> holidays);
}
