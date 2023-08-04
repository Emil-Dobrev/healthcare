package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;

import java.util.List;

public interface DoctorService  {

    DoctorDTO getDoctorById(Long id);

    List<DoctorDTO> getAllDoctors();

    DoctorDTO updateDoctor(Long id, String role, UpdateDoctorRequest updateDoctorRequest);
}
