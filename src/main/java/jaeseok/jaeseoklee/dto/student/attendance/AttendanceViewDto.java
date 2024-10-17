package jaeseok.jaeseoklee.dto.student.attendance;

import jaeseok.jaeseoklee.entity.student.Attendance;
import jaeseok.jaeseoklee.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceViewDto {
    private String studentName;
    private Grade studentGrade;
    private int classNum;
    private Attendance attendanceStatus;
    private String attendanceReason;
}
