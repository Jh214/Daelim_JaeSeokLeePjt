package jaeseok.jaeseoklee.dto.schedule;

import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.Period;
import jaeseok.jaeseoklee.entity.schedule.TimeSlot;
import lombok.*;
import org.aspectj.weaver.patterns.PerObject;
import org.hibernate.internal.build.AllowNonPortable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRegisterDto {
    private String subject;
    private Period period;
    private DayOfWeek dayOfWeek;
    private Long uid;
}
