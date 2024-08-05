package jaeseok.jaeseoklee.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDto {
    private String studentName;
    private String studentNum;
    private String studentAge;
    private Long uid;
    private Long studentId;
}
