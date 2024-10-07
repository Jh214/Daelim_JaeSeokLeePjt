package jaeseok.jaeseoklee.repository.websocket;

import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.creator.userId = :masterId")
    List<ChatRoom> findByUserId(@Param("masterId") String masterId);
}