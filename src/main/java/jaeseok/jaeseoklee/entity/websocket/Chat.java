package jaeseok.jaeseoklee.entity.websocket;

import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Table(name = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "message")
    private String message;

    @CreationTimestamp
    @Column(name = "sendTime")
    private LocalDateTime sendTime;

}
