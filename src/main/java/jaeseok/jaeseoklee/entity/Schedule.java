package jaeseok.jaeseoklee.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "schedule")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @Column(name = "scheduleSub", nullable = false)
    private String scheduleSub; // 수업 과목
    @Column(name = "location", nullable = false)
    private String location; // 수업 장소


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    private User user;
}
