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
    private String masterName;
    private String participantName;
    private String lastMessages;

    @Builder
    public ChatRoomDto(Long chatRoomId, String masterId, String name, String participantId, String masterName, String participantName, String lastMessages) {
        this.chatRoomId = chatRoomId;
        this.masterId = masterId;
        this.participantId = participantId;
        this.name = name;
        this.masterName = masterName;
        this.participantName = participantName;
        this.lastMessages = lastMessages;
    }
}
