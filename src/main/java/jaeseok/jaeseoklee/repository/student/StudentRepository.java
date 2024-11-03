package jaeseok.jaeseoklee.repository.student;

import jaeseok.jaeseoklee.entity.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, StudentRepositoryCustom {
    @Query("SELECT s FROM Student s WHERE s.user.userId = :userId")
    Page<Student> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.user.userId = :userId")
    List<Student> findStudentListByUserId(@Param("userId") String userId);

    boolean existsByStudentNumAndUser_UserId(@Param("studentNum") String studentNum, @Param("userId") String userId);

    // 특정 사용자의 studentCode 존재 여부 확인
    boolean existsByStudentCodeAndUser_UserId(@Param("studentCode") int studentCode, @Param("userId") String userId);

    boolean existsByStudentNumAndUser_UserIdAndStudentIdNot(String studentNum, String userId, Long studentId);

    // 특정 유저 소속 학생들 중에서 중복된 studentCode가 있는지 확인 (단, 수정 중인 studentId는 제외)
    boolean existsByStudentCodeAndUser_UserIdAndStudentIdNot(int studentCode, String userId, Long studentId);

}
