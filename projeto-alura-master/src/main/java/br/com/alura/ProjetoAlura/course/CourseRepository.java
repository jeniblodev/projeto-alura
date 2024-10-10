package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.registration.RegistrationReportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    Course findByCode(String code);

    @Query(value = "SELECT c.name as courseName, c.code as courseCode, i.name as UserName, i.email as " +
            "UserEmail, COUNT(e.id) as totalEnrollments " +
            "FROM Course c " +
            "JOIN User i ON c.instructor_id = i.id " +
            "JOIN Enrollments e ON c.id = e.course_id " +
            "GROUP BY c.id, c.name, i.name, i.email " +
            "ORDER BY totalEnrollments DESC", nativeQuery = true)
    List<Object[]> findCoursesWithEnrollmentCount();
}
