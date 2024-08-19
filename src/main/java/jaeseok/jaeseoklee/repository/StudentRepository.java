package jaeseok.jaeseoklee.repository;

import jaeseok.jaeseoklee.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.user.userId = :userId")
    Page<Student> findByUserId(@Param("userId") String userId, Pageable pageable);

}
