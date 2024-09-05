package jaeseok.jaeseoklee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "chat")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    @Column(name = "chat_room_id")
    private Long chatRoomId;
    @Column(name = "message")
    private String message;
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "rec_id")
    private String recId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    @JsonIgnore // 응답 데이터에서 제외시키는 어노테이션
    private User user;
}
