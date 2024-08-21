package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDetailDto {
    private String userId;
    private String userName;
    private String userNum;
    private String userDate;
    private String userEmail;
    private String schoolName;
    private int classNum;
}
