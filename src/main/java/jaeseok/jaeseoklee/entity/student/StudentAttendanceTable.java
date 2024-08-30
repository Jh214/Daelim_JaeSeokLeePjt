package jaeseok.jaeseoklee.entity.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_attendance_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentAttendanceTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    @Enumerated(EnumType.STRING)
    private Attendance attendanceStatus;
    @Column(name = "attendace_reason")
    private String attendanceReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Student student;

}