package jaeseok.jaeseoklee.repository.student;

import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositoryCustom {
    Page<Student> findStudentsByUserIdAndGrade(StudentFilterDto studentFilterDto, Pageable pageable);
}
