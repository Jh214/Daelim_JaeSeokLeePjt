package jaeseok.jaeseoklee.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryViewDto {
    private String message;
    private LocalDateTime timestamp;
}
