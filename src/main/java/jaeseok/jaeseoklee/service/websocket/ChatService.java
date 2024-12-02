package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.websocket.ChatDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChat;
import jaeseok.jaeseoklee.dto.websocket.ResponseChat;
import org.springframework.data.domain.Page;

public interface ChatService {

    void saveMessage(ChatDto chatDto);

    Page<ResponseChat> getChats(Long chatRoomId, RequestChat requestChat);
}
