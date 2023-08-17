package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.*;
import emil.dobrev.services.enums.DoctorSpecialization;

import java.util.List;

public interface DoctorService {

    DoctorDTO getDoctorById(Long id);

    List<DoctorDTO> getAllDoctors(DoctorSpecialization specialization);

    DoctorDTO updateDoctor(Long id, UpdateDoctorRequest updateDoctorRequest);

    CommentDTO addComment(Long doctorId, Long patientId, CommentRequest request);

    List<CommentDTO> getAllCommentsForDoctor(Long doctorId);

    void voteForDoctor(Long doctorId, Long patientId, VoteRequest request);
}
