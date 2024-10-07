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
    private String masterId;

    @NotBlank
    private String participantId;

    @NotBlank
    private String name;

    @Builder
    public RequestChatRoom(String masterId, String name, String participantId) {
        this.masterId = masterId;
        this.name = name;
        this.participantId = participantId;
    }

    public ChatRoom toEntity(User creator, User participant) {
        return ChatRoom.builder()
                .creator(creator)  // masterId에 해당하는 User 설정
                .participant(participant)  // participantId에 해당하는 User 설정
                .name(name)
                .build();
    }
}
