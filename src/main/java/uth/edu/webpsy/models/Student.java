package uth.edu.webpsy.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Tránh vòng lặp khi trả về JSON
    private User user;

    @Column(name = "major", nullable = false, length = 100)
    private String major;

    //Constructor
    public Student(Long id, User user, String major) {
        this.id = id;
        this.user = user;
        this.major = major;
    }
    public Student() {}

    //Getter, Setter

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMajor() {
        return major;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
