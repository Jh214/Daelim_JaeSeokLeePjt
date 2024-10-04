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
    public ResponseChatRoom(List<ChatRoom> chatRoomList) {
        this.chatRoomDtoList = chatRoomList.stream()
                .map(chatRoom -> ChatRoomDto.builder()
                        .chatRoomId(chatRoom.getChatRoomId())
                        .masterId(chatRoom.getUser().getUid())
                        .name(chatRoom.getName())
                        .clubId(chatRoom.getClubId())
                        .build())
                .collect(Collectors.toList());
    }
}
