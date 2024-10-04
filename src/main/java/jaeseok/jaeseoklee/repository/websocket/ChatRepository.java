package jaeseok.jaeseoklee.repository.websocket;

import jaeseok.jaeseoklee.entity.websocket.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
}
