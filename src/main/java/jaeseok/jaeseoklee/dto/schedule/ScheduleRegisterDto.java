package jaeseok.jaeseoklee.dto.schedule;

import lombok.*;
import org.hibernate.internal.build.AllowNonPortable;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleRegisterDto {
    private String scheduleSub;
    private String location;
    private Long uid;
}
