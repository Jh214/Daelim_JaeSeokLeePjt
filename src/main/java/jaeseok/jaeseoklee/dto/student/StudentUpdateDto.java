package jaeseok.jaeseoklee.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto {
    private String studentName;
    private String studentNum;
    private String studentDate;
    private Long uid;
    private Long studentId;
}
