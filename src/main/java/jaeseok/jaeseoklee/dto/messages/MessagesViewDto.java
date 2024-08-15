package jaeseok.jaeseoklee.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessagesViewDto {
    private String message;
    private LocalDateTime timestamp;
}
