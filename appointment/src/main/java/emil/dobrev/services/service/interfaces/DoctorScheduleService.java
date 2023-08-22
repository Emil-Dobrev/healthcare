package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.VacationResponse;
import emil.dobrev.services.dto.VacationRequest;
import emil.dobrev.services.dto.ScheduleRequest;

import java.util.List;

public interface DoctorScheduleService {
    void createSchedule(Long id, String roles, ScheduleRequest request);

    DoctorScheduleDTO getSchedule(Long id);

    void createVacation(Long id, String roles, VacationRequest holidays);

    void updateVacation(Long userId, String roles, Long holidayId, VacationRequest request);

    DoctorScheduleDTO updateSchedule(Long userId, String roles, Long scheduleID, ScheduleRequest request);

    List<VacationResponse> getALlVacationsForDoctor(Long doctorId, String roles);

    VacationResponse getVacationById(Long userId, String roles, Long holidayId);
}
