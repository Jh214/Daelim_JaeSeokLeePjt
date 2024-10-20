package jaeseok.jaeseoklee.dto.user;

import jaeseok.jaeseoklee.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private String userName;
    private String userNum;
    private String schoolName;
    private int classNum;
    private Grade grade;
}
