package jaeseok.jaeseoklee.repository.student;

import jaeseok.jaeseoklee.dto.student.StudentFilterDto;
import jaeseok.jaeseoklee.entity.student.Grade;
import jaeseok.jaeseoklee.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface StudentRepositoryCustom {
    Page<Student> findStudentsByUserIdAndGrade(StudentFilterDto studentFilterDto, Pageable pageable);
}
