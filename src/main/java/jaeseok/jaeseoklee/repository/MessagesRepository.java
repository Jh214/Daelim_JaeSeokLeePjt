package jaeseok.jaeseoklee.repository;

import jaeseok.jaeseoklee.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT m FROM Chat m WHERE m.user.userId = :userId")
    List<Chat> findByUserIdOrderByTimestampAsc(@Param("userId") String userId);

    @Query("SELECT m FROM Chat m WHERE m.recId = :recId")
    List<Chat> findByRecIdOrderByTimestampAsc(@Param("recId") String recId);
}
