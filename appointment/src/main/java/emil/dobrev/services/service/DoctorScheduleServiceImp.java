package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateScheduleRequest;
import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.DoctorScheduleService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static emil.dobrev.services.constant.Constants.DOCTOR;
import static emil.dobrev.services.constant.Constants.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImp implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createSchedule(Long doctorId, String roles, CreateScheduleRequest request) {
        checkForDoctorPermission(roles);
        var doctorSchedule = DoctorSchedule
                .builder()
                .doctorId(doctorId)
                .workingDays(request.workingDays())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .breakTo(request.breakTo())
                .breakFrom(request.breakFrom())
                .build();
        doctorScheduleRepository.save(doctorSchedule);
    }

    @Override
    public DoctorScheduleDTO getSchedule(Long id) {
        var schedule = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No schedule with id: " + id));
        return modelMapper.map(schedule, DoctorScheduleDTO.class);
    }

    @Override
    public void setHoliday(Long id, String roles, List<LocalDate> holidays) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findByDoctorId(id)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with id:" + id));
        schedule.setHoliday(holidays);
        doctorScheduleRepository.save(schedule);
    }

    private void checkForDoctorPermission(String roles) {
        if (!roles.contains(DOCTOR)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }
}
