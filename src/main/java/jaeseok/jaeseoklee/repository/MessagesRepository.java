package jaeseok.jaeseoklee.repository;

import jaeseok.jaeseoklee.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    @Query("SELECT m FROM Messages m WHERE m.user.userId = :userId")
    List<Messages> findByUserIdOrderByTimestampAsc(@Param("userId") String userId);

    @Query("SELECT m FROM Messages m WHERE m.recId = :recId")
    List<Messages> findByRecIdOrderByTimestampAsc(@Param("recId") String recId);
}
