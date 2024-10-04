package jaeseok.jaeseoklee.repository.websocket;

import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByClubId(Long clubId);
}