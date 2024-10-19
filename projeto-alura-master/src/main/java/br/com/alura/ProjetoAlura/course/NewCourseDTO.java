package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import static br.com.alura.ProjetoAlura.user.Role.STUDENT;

public class NewCourseDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Pattern(regexp = "[a-z\\-]+", message = "O código do curso deve conter apenas letras e hífen")
    @Size(min = 4, max = 10, message = "O código do curso deve ter entre 4 e 10 caracteres")
    private String code;

    private String description;

    @NotNull
    @NotBlank
    @Email
    private String instructorEmail;

    private Status status;

    public NewCourseDTO() {
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course toModel(User instructor) { return new Course(name, code, description, instructor);}
}
