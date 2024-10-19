package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserController;
import br.com.alura.ProjetoAlura.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newCourse__should_return_created_when_course_request_is_valid() throws Exception {
        User instructor = new User("Jeniffer Bittencourt", "jeniffer@alura.com", Role.INSTRUCTOR, "password123");
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Aplicando LINQ no C#");
        newCourseDTO.setCode("linq-net");
        newCourseDTO.setDescription("Aprenda a utilizar LINQ nos seus projetos .NET");
        newCourseDTO.setInstructorEmail("jeniffer@email.com");

        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(instructor);
        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(false);

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    public void newCourse__should_return_bad_request_when_role_user_is_invalid() throws Exception {
        User instructor = new User("Daniel Portugal", "daniel@alura.com", Role.STUDENT, "passwor123");
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Testes com .NET");
        newCourseDTO.setCode("test-net");
        newCourseDTO.setDescription("Aprenda aplicar testes de unidade em seus projetos .NET");
        newCourseDTO.setInstructorEmail("daniel@email.com");

        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(instructor);

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Este usuário não está registrado como instrutor e somente " +
                        "instrutores podem ser vinculados a cursos."));
    }

    @Test
    public void newCourse__should_return_bad_request_when_course_code_exists() throws Exception {
        User instructor = new User("Jeniffer Bittencourt", "jeniffer@alura.com", Role.INSTRUCTOR, "password123");
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Aplicando LINQ no C#");
        newCourseDTO.setCode("linq-net");
        newCourseDTO.setDescription("Aprenda a utilizar LINQ nos seus projetos .NET");
        newCourseDTO.setInstructorEmail("jeniffer@alura.com");

        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(instructor);
        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(true);
        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Código já cadastrado no sistema"));
    }

    @Test
    public void inactivateCourse__should_return_ok_when_course_code_is_valid() throws Exception {
        String courseCode = "linq-net";
        User instructor = new User("Jeniffer Bittencourt", "jeniffer@alura.com", Role.INSTRUCTOR, "password123");
        Course course = new Course("Aplicando LINQ no C#", courseCode, "Aprenda a utilizar o LINQ", instructor);

        when(courseRepository.findByCode(courseCode)).thenReturn(course);

        mockMvc.perform(post("/course/{code}/inactive", courseCode))
                .andExpect(status().isOk());
    }

    @Test
    public void inactivateCourse__should_return_bad_request_when_course_is_already_inactive() throws Exception {
        String courseCode = "linq-net";
        User instructor = new User("Jeniffer Bittencourt", "jeniffer@alura.com", Role.INSTRUCTOR, "password123");
        Course course = new Course("Aplicando LINQ no C#", courseCode, "Aprenda a utilizar o LINQ", instructor);
        course.setStatus(Status.INACTIVE);

        when(courseRepository.findByCode(courseCode)).thenReturn(course);

        mockMvc.perform(post("/course/{code}/inactive", courseCode))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Curso já está inativado."));
    }

    @Test
    public void inactivateCourse__should_return_bad_request_when_course_not_found() throws Exception {
        String courseCode = "course_not_found";

        when(courseRepository.findByCode(courseCode)).thenReturn(null);

        mockMvc.perform(post("/course/{code}/inactive", courseCode))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Código de curso não localizado."));
    }
}
