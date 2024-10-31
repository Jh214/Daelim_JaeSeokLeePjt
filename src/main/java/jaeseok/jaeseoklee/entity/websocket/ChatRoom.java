package jaeseok.jaeseoklee.entity.websocket;

import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Comparator;
import java.util.List;


@Table(name = "chat_room")
@Getter
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
@AllArgsConstructor
@Entity
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

//    방 생성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_uid", referencedColumnName = "uid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User creator;

    // 두 번째 User 참가자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_uid", referencedColumnName = "uid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User participant;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chat;

    public Chat getLatestMessage() {
        return chat.stream()
                .max(Comparator.comparing(Chat::getSendTime)) // 메시지의 생성 시간을 기준으로 가장 최근 메시지를 선택
                .orElse(null); // 메시지가 없을 경우 null 반환
    }

}
