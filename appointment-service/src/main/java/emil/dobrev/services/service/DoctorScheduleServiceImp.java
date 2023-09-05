package emil.dobrev.services.service;

import emil.dobrev.services.dto.*;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.model.DoctorVacation;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static emil.dobrev.services.constant.Constants.UNAUTHORIZED;
import static emil.dobrev.services.shared.PermissionsUtils.checkForDoctorPermission;

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

        if (!Objects.equals(schedule.getDoctorId(), doctorId)) {
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
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("No schedule with id: " + doctorId));
        return modelMapper.map(schedule, DoctorScheduleDTO.class);
    }

    @Override
    public void createVacation(Long doctorId, String roles, VacationRequest request) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));

        var allConfirmedVacations = schedule.getVacation().stream()
                .flatMap(vacation -> vacation.getVacationDate().stream())
                .toList();

        //check if some date already is required for vacation and if yes , removes it from the list
        var vacation = request.holidays()
                .stream()
                .filter(vacationDate -> !allConfirmedVacations.contains(vacationDate))
                .toList();

        schedule.getVacation().add(
                DoctorVacation.builder()
                        .vacationDate(vacation)
                        .doctor(schedule)
                        .build()
        );
        doctorScheduleRepository.save(schedule);
    }

    @Override
    public List<VacationResponse> getALlVacationsForDoctor(Long doctorId, String roles) {
        checkForDoctorPermission(roles);
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));
        var holidays = doctorScheduleRepository.getAllVacationsForDoctor(schedule.getId())
                .orElseThrow(() -> new NotFoundException("No available holidays for user with id:" + doctorId));
        var mapOfHolidays = holidays.stream()
                .collect(Collectors.groupingBy(
                        Vacation::getVacationId,
                        Collectors.mapping(Vacation::getVacationDate, Collectors.toList())
                ));

        return mapOfHolidays
                .entrySet()
                .stream()
                .map(entry -> new VacationResponse(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
    }

    @Override
    public VacationResponse getVacationById(Long userId, String roles, Long holidayId) {
        var holidays = doctorScheduleRepository.getVacationById(holidayId)
                .orElseThrow(() -> new NotFoundException("No holiday with id: " + holidayId));
        var holidayDays = holidays.stream().map(Vacation::getVacationDate).toList();

        return new VacationResponse(holidayId, holidayDays);
    }

    @Transactional
    @Override
    public void updateVacation(Long doctorId, String roles, Long holidayId, VacationRequest request) {
        checkForDoctorPermission(roles);
        doctorScheduleRepository.deleteHolidayDates(holidayId);
        createVacation(doctorId, roles, request);
    }
}
