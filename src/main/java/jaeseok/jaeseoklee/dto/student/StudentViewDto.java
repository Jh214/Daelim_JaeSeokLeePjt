package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.student.Attendance;
import jaeseok.jaeseoklee.entity.student.Grade;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentViewDto {
    private String studentName;
    private String studentNum;
    private String studentGender;
    private String studentAge;
    private String schoolName;
    private Grade studentGrade;
    private int classNum;
}
