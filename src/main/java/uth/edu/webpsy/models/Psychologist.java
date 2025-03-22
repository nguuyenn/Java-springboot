package uth.edu.webpsy.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "psychologists")
public class Psychologist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization; // Lĩnh vực chuyên môn

    //Constructor
    public Psychologist(Long id, User user, String specialization) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
    }
    public Psychologist() {}

    //Getter, Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getSpecialization() { return specialization;}
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
