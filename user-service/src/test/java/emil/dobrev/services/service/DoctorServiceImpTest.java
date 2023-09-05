package emil.dobrev.services.service;

import emil.dobrev.services.dto.CommentRequest;
import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;
import emil.dobrev.services.dto.VoteRequest;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Comment;
import emil.dobrev.services.model.Doctor;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.repository.CommentRepository;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImpTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private DoctorServiceImp doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {

        this.doctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("Texas")
                .roles(List.of(Role.DOCTOR))
                .password("oldPassword")
                .birthdate(LocalDate.of(1980, 1, 1))
                .phoneNumber("12345678")
                .specialization(DoctorSpecialization.CARDIOLOGY)
                .age(43)
                .votedUsers(new HashMap<>())
                .email("johnDoe@gmail.com")
                .build();
    }

    @Test
    void shouldUpdateDoctor() {

        // Create a mock doctor
        Doctor existingDoctor = this.doctor;


        // Create a mock repository
        when(doctorRepository.findById(existingDoctor.getId()))
                .thenReturn(java.util.Optional.of(existingDoctor));

        // Create a mock password encoder
        when(passwordEncoder.encode(any())).thenReturn("newHashedPassword");

        UpdateDoctorRequest updateRequest = new UpdateDoctorRequest(
                "Jane",
                "Smith",
                "newPassword",
                LocalDate.of(1990, 2, 2),
                DoctorSpecialization.DERMATOLOGY,
                "987654321"
        );

        DoctorDTO expectedUpdatedDoctorDTO = DoctorDTO.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .phoneNumber("987654321")
                .specialization(DoctorSpecialization.DERMATOLOGY)
                .address("Texas")
                .email("johnDoe@gmail.com")
                .build();
        // Set properties on expectedUpdatedDoctorDTO as required

        // Set up the behavior of the modelMapper mock
        when(modelMapper.map(any(), eq(DoctorDTO.class))).thenReturn(expectedUpdatedDoctorDTO);

        // Call the update method
        DoctorDTO updatedDoctor = doctorService.updateDoctor(existingDoctor.getId(), updateRequest);

        ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository).save(doctorCaptor.capture());

        Doctor updatedDoctorArgument = doctorCaptor.getValue();

        assertEquals("Jane", updatedDoctorArgument.getFirstName());
        assertEquals("Smith", updatedDoctorArgument.getLastName());
        assertEquals("newHashedPassword", updatedDoctorArgument.getPassword());
        assertEquals(LocalDate.of(1990, 2, 2), updatedDoctorArgument.getBirthdate());
        assertEquals(DoctorSpecialization.DERMATOLOGY, updatedDoctorArgument.getSpecialization());
        assertEquals("987654321", updatedDoctorArgument.getPhoneNumber());

        // Verify that the returned DTO matches the updated doctor
        assertEquals(updatedDoctorArgument.getId(), updatedDoctor.getId());
        assertEquals(updatedDoctorArgument.getFirstName(), updatedDoctor.getFirstName());
        assertEquals(updatedDoctorArgument.getLastName(), updatedDoctor.getLastName());
        assertEquals(updatedDoctorArgument.getSpecialization(), updatedDoctor.getSpecialization());
        assertEquals(updatedDoctorArgument.getPhoneNumber(), updatedDoctor.getPhoneNumber());
    }

    @Test
    void shouldVoteForDoctor() {

        Long patientId = 2L;
        HashMap<Long, Double> votedUsers = new HashMap<>();
        votedUsers.put(1L, 6.0);
        doctor.setVotedUsers(votedUsers);

        when(doctorRepository.findById(1l))
                .thenReturn(java.util.Optional.of(doctor));

        var request = new VoteRequest(4.5);

        doctorService.voteForDoctor(doctor.getId(), patientId, request);

        verify(doctorRepository, times(1)).save(any(Doctor.class));

        assertEquals(doctor.getRating(), 5.25);
        assertEquals(doctor.getVotedUsers().size(), 2);

    }

    @Test
    void shouldAddComment() {

        var patientId = 1L;
        var patient = Patient.builder()
                .id(patientId)
                .firstName("Mike")
                .lastName("Gilbert")
                .birthdate(LocalDate.of(1993, 4, 13))
                .build();
        String patientFullName = String.format("%s %s", patient.getFirstName(), patient.getLastName()).trim();

        var request = new CommentRequest("description");

        when(doctorRepository.findById(1l))
                .thenReturn(java.util.Optional.of(doctor));

        when(patientRepository.findById(1l))
                .thenReturn(java.util.Optional.of(patient));

        var result = doctorService.addComment(doctor.getId(), patientId, request);

        verify(commentRepository, times(1)).save(any(Comment.class));

        assertEquals(result.description(), request.description());
        assertEquals(result.createdBy(), patientFullName);
    }
}



