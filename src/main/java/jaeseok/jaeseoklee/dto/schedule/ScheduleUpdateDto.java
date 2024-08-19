package jaeseok.jaeseoklee.dto.schedule;

import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduleUpdateDto {
    private String subject;
    private String location;
    private TimeSlot startTime;
    private TimeSlot endTime;
    private DayOfWeek dayOfWeek;
    private Long uid;
    private Long scheduleId;
}
