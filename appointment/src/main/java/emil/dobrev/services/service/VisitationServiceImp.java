package emil.dobrev.services.service;

import emil.dobrev.services.dto.VisitationDTO;
import emil.dobrev.services.dto.VisitationRequest;
import emil.dobrev.services.exception.AlreadyExistException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.VisitationRecord;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.VisitationRepository;
import emil.dobrev.services.service.interfaces.VisitationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static emil.dobrev.services.shared.PermissionsUtils.checkForDoctorPermission;

@Service
@RequiredArgsConstructor
public class VisitationServiceImp implements VisitationService {

    private final VisitationRepository visitationRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public VisitationDTO addVisitation(
            VisitationRequest visitationRequest,
            Long doctorId,
            String roles
    ) {
        checkForDoctorPermission(roles);
        var appointment = appointmentRepository.findById(visitationRequest.appointmentId())
                .orElseThrow(() -> new NotFoundException("No appointment with id: " + visitationRequest.appointmentId()));
        if (!doctorId.equals(appointment.getDoctorId())) {
            throw new UnauthorizedException("No permissions for doctor with id: " + doctorId);
        }

        if (visitationRepository.findByAppointmentId(visitationRequest.appointmentId()).isPresent()) {
            throw new AlreadyExistException("Visitation already exist");
        }
        var visitation = VisitationRecord.builder()
                .appointmentId(appointment.getId())
                .visitDateTime(appointment.getAppointmentDateTime())
                .diagnosis(visitationRequest.diagnosis())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .treatmentPlan(visitationRequest.treatmentPlan())
                .build();
        visitationRepository.save(visitation);
        return modelMapper.map(visitation, VisitationDTO.class);
    }

    @Override
    public List<VisitationDTO> getAllVisitationsForPatient(Long userId, String roles, Long patientId) {
        if (!userId.equals(patientId) && !checkForDoctorPermission(roles)) {
            throw new UnauthorizedException("You don't have permission");
        }
        return visitationRepository.getAllByPatientId(patientId)
                .stream()
                .map(visitationRecord -> modelMapper.map(visitationRecord, VisitationDTO.class))
                .toList();
    }
}
