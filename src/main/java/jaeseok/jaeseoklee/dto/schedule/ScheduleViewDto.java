package jaeseok.jaeseoklee.dto.schedule;

import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.Period;
import jaeseok.jaeseoklee.entity.schedule.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleViewDto {
    private String subject;
    private Period period;
    private DayOfWeek dayOfWeek;
}
