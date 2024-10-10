package jaeseok.jaeseoklee.entity.websocket;

import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

}
