package jaeseok.jaeseoklee.entity.student;

import jaeseok.jaeseoklee.dto.student.StudentUpdateDto;
import jaeseok.jaeseoklee.entity.SeatTable;
import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Table(name = "Student")
@Entity
@Data
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
    @Column(nullable = false, name = "studentAge")
    private String studentAge;
    @Column(nullable = false, name = "schoolName")
    private String schoolName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "studentGrade")
    private Grade studentGrade;
    @Column(nullable = false, name = "classNum")
    private int classNum;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "student", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<SeatTable> seatTable;

    public void update(StudentUpdateDto updateDto){
        this.studentName = updateDto.getStudentName();
        this.studentNum = updateDto.getStudentNum();
        this.studentAge = updateDto.getStudentAge();
    }
}
