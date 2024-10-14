package jaeseok.jaeseoklee.dto.user.find;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindUserPasswordByUserNum {
    private String userId;
    private String userNum;
    private int ranCode = (int) (Math.random() * (1000000 - 100000)) + 100000;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);
}
