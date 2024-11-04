package jaeseok.jaeseoklee.dto.websocket;

import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ResponseChatRoom {

    List<ChatRoomDto> chatRoomDtoList;

    @Builder
    public ResponseChatRoom(List<ChatRoomDto> chatRoomDtoList) {
        this.chatRoomDtoList = chatRoomDtoList;
    }
}
