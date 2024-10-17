package jaeseok.jaeseoklee.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_attendance_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    @Enumerated(EnumType.STRING)
    private Attendance attendanceStatus;
    @Column(name = "attendance_reason")
    private String attendanceReason;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

}
