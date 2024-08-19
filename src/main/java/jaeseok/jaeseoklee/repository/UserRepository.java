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

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);

//    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    boolean existsByUserId(@Param("userId") String userId);

//    @Query("SELECT u FROM User u WHERE u.userEmail = :userEmail")
    boolean existsByUserEmail(@Param("userEmail") String userEmail);

//    @Query("SELECT u FROM User u WHERE u.userNum = :userNum")
    boolean existsByUserNum(@Param("userNum") String userNum);
}
