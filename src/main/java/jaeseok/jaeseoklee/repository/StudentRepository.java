package jaeseok.jaeseoklee.repository;

import jaeseok.jaeseoklee.entity.Student;
import jaeseok.jaeseoklee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.user.userId = :userId")
    Page<Student> findByUserId(@Param("userId") String userId, Pageable pageable);

}
