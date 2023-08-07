package emil.dobrev.services.service;

import emil.dobrev.services.dto.*;
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

import java.util.List;
import java.util.stream.Collectors;

import static emil.dobrev.services.constant.Constants.DOCTOR;
import static emil.dobrev.services.constant.Constants.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImp implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createSchedule(Long doctorId, String roles, ScheduleRequest request) {
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

    @Transactional
    @Override
    public DoctorScheduleDTO updateSchedule(Long doctorId, String roles, Long scheduleId, ScheduleRequest request) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("No schedule with id: " + doctorId));

        if (schedule.getDoctorId() != doctorId) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }

        return doctorScheduleRepository.updateSchedule(
                scheduleId,
                request.workingDays(),
                request.startTime(),
                request.endTime(),
                request.breakFrom(),
                request.breakTo()
        );

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

    @Override
    public List<HolidayResponse> getAllHolidaysForDoctor(Long doctorId, String roles) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));
        var holidays = doctorScheduleRepository.getAllHolidays(schedule.getId());
        var mapOfHolidays = holidays.stream()
                .collect(Collectors.groupingBy(
                        Response::getHolidayId,
                        Collectors.mapping(Response::getHolidayDate, Collectors.toList())
                ));

        return mapOfHolidays
                .entrySet()
                .stream()
                .map(entry -> new HolidayResponse(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
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
