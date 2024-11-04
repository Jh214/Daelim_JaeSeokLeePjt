package jaeseok.jaeseoklee.dto.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomDto {

    private Long chatRoomId; // 방 번호
    private String masterId;
    private String participantId;
    private String name;
    private String lastMessages;
    private String lastMessageTime;

    @Builder
    public ChatRoomDto(Long chatRoomId, String masterId, String name, String participantId, String lastMessages, String lastMessagesTime) {
        this.chatRoomId = chatRoomId;
        this.masterId = masterId;
        this.participantId = participantId;
        this.name = name;
        this.lastMessages = lastMessages;
        this.lastMessageTime = lastMessagesTime;
    }
}
