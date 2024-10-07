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

    @Builder
    public ChatRoomDto(Long chatRoomId, String masterId, String name, String participantId) {
        this.chatRoomId = chatRoomId;
        this.masterId = masterId;
        this.participantId = participantId;
        this.name = name;
    }
}
