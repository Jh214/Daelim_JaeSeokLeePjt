package jaeseok.jaeseoklee.dto.user;

import jaeseok.jaeseoklee.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {
    private String userId;
    private String userRealName;
    private String userNum;
    private String userDate;
    private String userEmail;
    private String schoolName;
    private int classNum;
    private Grade grade;
}
