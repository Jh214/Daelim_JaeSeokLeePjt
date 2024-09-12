package jaeseok.jaeseoklee.dto.student;

import jaeseok.jaeseoklee.entity.student.Attendance;
import jaeseok.jaeseoklee.entity.student.Grade;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentViewDto {
    private String studentName;
    private String studentNum;
    private String studentGender;
    private String studentDate;
    private Grade studentGrade;
    private int classNum;
}
