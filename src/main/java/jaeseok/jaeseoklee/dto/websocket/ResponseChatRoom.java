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
                        .masterId(chatRoom.getCreator().getUserId())
                        .name(chatRoom.getName())
                        .participantId(chatRoom.getParticipant().getUserId())
                        .build())
                .collect(Collectors.toList());
    }
}
