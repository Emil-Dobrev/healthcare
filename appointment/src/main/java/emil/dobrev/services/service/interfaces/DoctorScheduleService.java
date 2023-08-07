package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.HolidayResponse;
import emil.dobrev.services.dto.HolidaysRequest;
import emil.dobrev.services.dto.ScheduleRequest;

import java.util.List;

public interface DoctorScheduleService {
    void createSchedule(Long id, String roles, ScheduleRequest request);

    DoctorScheduleDTO getSchedule(Long id);

    void setHolidays(Long id, String roles, HolidaysRequest holidays);

    void updateHolidays(Long userId, String roles, Long holidayId, HolidaysRequest request);

    DoctorScheduleDTO updateSchedule(Long userId, String roles, Long scheduleID, ScheduleRequest request);

    List<HolidayResponse> getAllHolidaysForDoctor(Long doctorId, String roles);
}
