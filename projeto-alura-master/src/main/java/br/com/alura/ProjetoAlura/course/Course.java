package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;

    @ManyToOne
    private User instructor;


    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "inactivation_date")
    private LocalDateTime inactivationDate = LocalDateTime.now();

    @Deprecated
    public Course() {}



    public Course(String name, String code, String description, Status status) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.status = status;
    }

    public String getName() { return name; }

    public String getCode() { return code; }

    public String getDescription() { return description; }

    public Status getStatus() { return status; }

    public User getInstructor() {
        return instructor;
    }

    public LocalDateTime getInactivationDate() { return inactivationDate; }
}
