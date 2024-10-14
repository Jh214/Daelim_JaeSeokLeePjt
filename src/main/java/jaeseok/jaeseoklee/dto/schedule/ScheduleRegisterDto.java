package jaeseok.jaeseoklee.dto.schedule;

import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.Period;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRegisterDto {
    private String subject;
    private Period period;
    private DayOfWeek dayOfWeek;
    private String userId;
}
