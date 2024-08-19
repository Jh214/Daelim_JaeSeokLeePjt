package jaeseok.jaeseoklee.entity.schedule;

import jaeseok.jaeseoklee.dto.schedule.ScheduleUpdateDto;
import jaeseok.jaeseoklee.entity.User;
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
    @Column(name = "subject", nullable = false)
    private String subject; // 수업 과목
    @Column(name = "location", nullable = false)
    private String location; // 수업 장소
    @Enumerated(EnumType.STRING)
    @Column(name = "start_time", nullable = false)
    private TimeSlot startTime; // ENUM 타입 시작 시간
    @Enumerated(EnumType.STRING)
    @Column(name = "end_time", nullable = false)
    private TimeSlot endTime; // ENUM 타입 종료 시간
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // ENUM 타입 수업 요일


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uid")
    private User user;

    public void update(ScheduleUpdateDto updateDto){
        this.subject = updateDto.getSubject();
        this.location = updateDto.getLocation();
        this.startTime = updateDto.getStartTime();
        this.endTime = updateDto.getEndTime();
        this.dayOfWeek = updateDto.getDayOfWeek();
    }
}
