package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.CreateScheduleRequest;
import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.HolidaysRequest;

public interface DoctorScheduleService {
    void createSchedule(Long id, String roles, CreateScheduleRequest request);

    DoctorScheduleDTO getSchedule(Long id);

    void setHolidays(Long id, String roles, HolidaysRequest holidays);

    void updateHolidays(Long userId, String roles, Long holidayId, HolidaysRequest request);
}
