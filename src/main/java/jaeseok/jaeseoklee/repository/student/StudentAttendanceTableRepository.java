package jaeseok.jaeseoklee.repository.student;

import jaeseok.jaeseoklee.entity.student.StudentAttendanceTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAttendanceTableRepository extends JpaRepository<StudentAttendanceTable, Long> {
}
