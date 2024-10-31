package jaeseok.jaeseoklee.repository.websocket;

import jaeseok.jaeseoklee.dto.websocket.ResponseChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositoryCustom {
    Page<ResponseChat> getChats(Long chatRoomId, Pageable pageable);
}
