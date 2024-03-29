package emil.dobrev.services.service;

import emil.dobrev.services.dto.*;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.exception.NoSuchElementException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.model.Comment;
import emil.dobrev.services.repository.CommentRepository;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static emil.dobrev.services.utils.JpaRepositoryUtils.getOrThrow;

@Service
@AllArgsConstructor
public class DoctorServiceImp implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final CommentRepository commentRepository;

    @Override
    public DoctorDTO getDoctorById(Long id) {
        var doctor = getOrThrow(doctorRepository, id);

        return modelMapper.map(doctor, DoctorDTO.class);
    }

    @Override
    public List<DoctorDTO> getAllDoctors(DoctorSpecialization specialization) {
        var spec = specialization != null ? specialization.toString() : null;
        var doctors = doctorRepository.findAll(spec);
        return doctors
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .toList();
    }

    @Override
    public DoctorDTO updateDoctor(Long id, UpdateDoctorRequest updateDoctorRequest) {
        var existingDoctor = getOrThrow(doctorRepository, id);

        if (!existingDoctor.getFirstName().equals(updateDoctorRequest.firstName())) {
            existingDoctor.setFirstName(updateDoctorRequest.firstName());
        }
        if (!existingDoctor.getLastName().equals(updateDoctorRequest.lastName())) {
            existingDoctor.setLastName(updateDoctorRequest.lastName());
        }
        if (updateDoctorRequest.password() != null && !passwordEncoder.encode(updateDoctorRequest.password()).matches(existingDoctor.getPassword())) {
            existingDoctor.setPassword(passwordEncoder.encode(updateDoctorRequest.password()));
        }
        if (!existingDoctor.getBirthdate().equals(updateDoctorRequest.birthdate())) {
            existingDoctor.setBirthdate(updateDoctorRequest.birthdate());
        }
        if (!existingDoctor.getSpecialization().equals(updateDoctorRequest.doctorSpecialization())) {
            existingDoctor.setSpecialization(updateDoctorRequest.doctorSpecialization());
        }
        if (!existingDoctor.getPhoneNumber().equals(updateDoctorRequest.phoneNumber())) {
            existingDoctor.setPhoneNumber(updateDoctorRequest.phoneNumber());
        }
        doctorRepository.save(existingDoctor);
        return modelMapper.map(existingDoctor, DoctorDTO.class);
    }

    @Override
    public CommentDTO addComment(Long doctorId, Long patientId, CommentRequest request) {

        var patient = getOrThrow(patientRepository, patientId);
        var doctor = getOrThrow(doctorRepository, doctorId);
        String patientFullName = String.format("%s %s", patient.getFirstName(), patient.getLastName()).trim();
        Comment comment = Comment.builder()
                .doctor(doctor)
                .createdBy(patientFullName)
                .description(request.description())
                .build();

        commentRepository.save(comment);

        return new CommentDTO(
                comment.getCreatedBy(),
                comment.getCreatedAt(),
                comment.getDescription()
        );
    }

    @Override
    public List<CommentDTO> getAllCommentsForDoctor(Long doctorId) {
        return commentRepository.findAllByDoctorId(doctorId)
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getCreatedBy(),
                        comment.getCreatedAt(),
                        comment.getDescription()))
                .toList();
    }

    @Override
    public void voteForDoctor(Long doctorId, Long patientId, VoteRequest request) {
        var doctor = getOrThrow(doctorRepository, doctorId);

        doctor.setVotedUsers(patientId, request.rating());
        doctor.setRating(calculateAverageRating(doctor.getVotedUsers()));

        doctorRepository.save(doctor);
    }

    private double calculateAverageRating(Map<Long, Double> votedUsers) {
        return votedUsers.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
