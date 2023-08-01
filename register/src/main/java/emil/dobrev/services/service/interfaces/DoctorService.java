package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.DoctorDTO;

import java.util.List;

public interface DoctorService  {

    DoctorDTO getDoctorById(Long id);

    List<DoctorDTO> getAllDoctors();
}
