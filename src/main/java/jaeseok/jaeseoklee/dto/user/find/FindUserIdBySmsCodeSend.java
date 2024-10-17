package jaeseok.jaeseoklee.dto.user.find;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FindUserIdBySmsCodeSend {
    private String userNum;
    private String content;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);
    private String userId;

    public FindUserIdBySmsCodeSend(String userNum, LocalDateTime timeLimit, String userId) {
        this.userNum = userNum;
        this.timeLimit = timeLimit;
        this.userId = userId;
        this.content = generateContent();
    }
    private String generateContent() {
        return userId;
    }
}
