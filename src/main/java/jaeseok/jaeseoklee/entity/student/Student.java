package jaeseok.jaeseoklee.entity.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.entity.Grade;
import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Table(name = "Student")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    @Column(nullable = false, length = 50, name = "sutdentName")
    private String studentName;
    @Column(nullable = false, length = 15, name = "studentNum")
    private String studentNum;
    @Column(nullable = false, name = "studentGender")
    private String studentGender;
    @Column(nullable = false, name = "studentDate")
    private String studentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "studentGrade")
    private Grade studentGrade;
    @Column(nullable = false, name = "classNum")
    private int classNum;
    @Column(nullable = false, name = "studentCode")
    private int studentCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "student", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<StudentAttendanceTable> studentAttendanceTable;

    public void update(StudentUpdateDto updateDto) {
        this.studentName = updateDto.getStudentName();
        this.studentNum = updateDto.getStudentNum().replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");
        this.studentCode = updateDto.getStudentCode();
    }
}
