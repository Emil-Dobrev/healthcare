//package emil.dobrev.services.service;
//
//import emil.dobrev.services.dto.DoctorDTO;
//import emil.dobrev.services.exception.NoSuchElementException;
//import emil.dobrev.services.service.interfaces.DoctorService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class DoctorServiceImp implements DoctorService {
//
//    private final DoctorRepository doctorRepository;
//    private final ModelMapper modelMapper;
//
//
//    @Override
//    public DoctorDTO getDoctorById(Long id) {
//        var doctor = doctorRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("No doctor with id: " + id));
//
//        return modelMapper.map(doctor, DoctorDTO.class);
//    }
//
//    @Override
//    public List<DoctorDTO> getAllDoctors() {
//        var doctors = doctorRepository.findAll();
//        return doctors
//                .stream()
//                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
//                .toList();
//    }
//
//
//}
