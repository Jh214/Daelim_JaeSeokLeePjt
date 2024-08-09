package jaeseok.jaeseoklee.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String schoolName;
    private int classNum;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    private User user;
}
