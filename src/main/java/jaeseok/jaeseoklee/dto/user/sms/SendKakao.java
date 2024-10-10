package jaeseok.jaeseoklee.dto.user.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SendKakao {
    private String userNum;
    private int ranCode = (int) (Math.random() * (1000000 - 100000)) + 100000;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);

    public SendKakao(String userNum, int ranCode, LocalDateTime timeLimit) {
        this.userNum = userNum;
        this.ranCode = ranCode;
        this.timeLimit = timeLimit;
    }

}
