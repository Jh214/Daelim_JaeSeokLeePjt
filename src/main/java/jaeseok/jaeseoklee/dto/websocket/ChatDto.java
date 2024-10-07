package jaeseok.jaeseoklee.dto.websocket;

import com.fasterxml.jackson.annotation.JsonFormat;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.websocket.Chat;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private Long chatRoomId; // 방 번호
    private String senderId;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendTime;
    private String userRealName;

    public Chat toEntity(User user, ChatRoom chatRoom) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .user(user)
                .message(message)
                .sendTime(sendTime)  // sendTime도 포함
                .build();
    }
}
