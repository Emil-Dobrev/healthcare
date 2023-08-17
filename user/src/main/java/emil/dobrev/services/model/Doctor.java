package emil.dobrev.services.model;

import emil.dobrev.services.enums.DoctorSpecialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class Doctor extends User {

    @Enumerated(EnumType.STRING)
    @Column
    private DoctorSpecialization specialization;
    @Column
    private String address;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();



    @ElementCollection
    @MapKeyColumn(name="voted_user_id")
    @Column(name="value")
    @CollectionTable(name="voted_users", joinColumns=@JoinColumn(name="doctor_id"))
    private Map<Long, Double> votedUsers = new HashMap<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setDoctor(this);
    }

    public void addVote(Long userId, Double stars) {
        this.votedUsers.put(userId, stars);
    }

    public Map<Long, Double> getVotedUsers() {
        return votedUsers;
    }

    public void setVotedUsers(Long patientId, Double stars) {
        this.votedUsers.put(patientId, stars);
    }
}
