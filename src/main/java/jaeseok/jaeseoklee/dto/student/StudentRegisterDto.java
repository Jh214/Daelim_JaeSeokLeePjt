package jaeseok.jaeseoklee.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterDto {
    private String studentName;
    private String studentNum;
    private String studentAge;
    private String studentGender;
    private String schoolName;
    private int classNum;
    private Long uid;
}
