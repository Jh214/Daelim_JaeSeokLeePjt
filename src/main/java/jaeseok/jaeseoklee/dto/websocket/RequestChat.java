package jaeseok.jaeseoklee.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChat {
    private Long chatRoomId;
    private int page = 0;
    private int size = 50;
}
