package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;
import emil.dobrev.services.enums.DoctorSpecialization;

import java.util.List;

public interface DoctorService {

    DoctorDTO getDoctorById(Long id);

    List<DoctorDTO> getAllDoctors(DoctorSpecialization specialization);

    DoctorDTO updateDoctor(Long id, UpdateDoctorRequest updateDoctorRequest);
}
