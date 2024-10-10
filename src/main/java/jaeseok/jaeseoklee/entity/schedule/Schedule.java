package jaeseok.jaeseoklee.entity.schedule;

import jaeseok.jaeseoklee.dto.schedule.ScheduleUpdateDto;
import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @Column(name = "subject", nullable = false)
    private String subject; // 수업 과목
    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period;
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // ENUM 타입 수업 요일


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    private User user;

    public void update(ScheduleUpdateDto updateDto) {
        this.subject = updateDto.getSubject();
        this.period = updateDto.getPeriod();
        this.dayOfWeek = updateDto.getDayOfWeek();
    }
}
