package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateScheduleRequest;
import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.HolidaysRequest;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.DoctorHoliday;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.DoctorScheduleService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DoctorScheduleDTO getSchedule(Long doctorId) {
        var schedule = doctorScheduleRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule with id: " + doctorId));
        return modelMapper.map(schedule, DoctorScheduleDTO.class);
    }

    @Override
    public void setHolidays(Long doctorId, String roles, HolidaysRequest request) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));
        schedule.getHoliday().add(
                DoctorHoliday.builder()
                        .holidayDate(request.holidays())
                        .doctor(schedule)
                        .build()
        );
        doctorScheduleRepository.save(schedule);
    }

    @Transactional
    @Override
    public void updateHolidays(Long doctorId, String roles, Long holidayId, HolidaysRequest request) {
        checkForDoctorPermission(roles);
        doctorScheduleRepository.deleteHolidayDates(holidayId);
        setHolidays(doctorId, roles, request);
    }

    private void checkForDoctorPermission(String roles) {
        if (!roles.contains(DOCTOR)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }
}
