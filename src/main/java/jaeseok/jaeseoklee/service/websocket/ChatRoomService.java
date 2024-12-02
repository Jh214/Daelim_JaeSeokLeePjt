package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChatRoom;


public interface ChatRoomService {

    ResponseDto<?> createChatRoom(RequestChatRoom requestChatRoom);

    ResponseDto<?> getChatRoom(String userId);

    ResponseDto<?> deleteChatRoom(Long chatRoomId);
}
