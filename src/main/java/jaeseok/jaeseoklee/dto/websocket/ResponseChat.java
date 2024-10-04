package jaeseok.jaeseoklee.dto.websocket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ResponseChat {
    private Long id;
    private Long chatRoomId; // 방 번호
    private Long senderId;
    private String userRealName;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalDateTime sendTime;
    private int page = 0;
    private int size = 50;

    @QueryProjection
    public ResponseChat(Long id, Long chatRoomId, Long senderId, String message, LocalDateTime sendTime, String userRealName) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.sendTime = sendTime;
        this.userRealName = userRealName;
    }
}