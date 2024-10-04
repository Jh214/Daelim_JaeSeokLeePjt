package jaeseok.jaeseoklee.dto.websocket;

import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestChatRoom {

    @NotBlank
    private Long masterId;

    @NotBlank
    private String name;

    @NotBlank
    private Long clubId;

    @Builder
    public RequestChatRoom(Long masterId, String name, Long clubId) {
        this.masterId = masterId;
        this.name = name;
        this.clubId = clubId;
    }

    public ChatRoom toEntity(User user) {
        return ChatRoom.builder()
                .user(user)  // 조회한 User 객체 설정
                .name(name)
                .clubId(clubId)
                .build();
    }
}
