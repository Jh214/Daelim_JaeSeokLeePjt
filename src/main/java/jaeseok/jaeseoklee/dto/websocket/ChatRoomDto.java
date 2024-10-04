package jaeseok.jaeseoklee.dto.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomDto {

    private Long chatRoomId; // 방 번호
    private Long masterId;
    private String name;
    private Long clubId;

    @Builder
    public ChatRoomDto(Long chatRoomId, Long masterId, String name, Long clubId) {
        this.chatRoomId = chatRoomId;
        this.masterId = masterId;
        this.name = name;
        this.clubId = clubId;
    }
}
