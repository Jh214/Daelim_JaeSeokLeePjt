package jaeseok.jaeseoklee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.schedule.TimeSlot;
import jaeseok.jaeseoklee.entity.student.Grade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//JackSon 설정파일 (제이슨 데이터 처리 하는 곳)
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Enum 타입에 빈 값이 들어가면 null 처리
        mapper.coercionConfigFor(Grade.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        mapper.coercionConfigFor(DayOfWeek.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        mapper.coercionConfigFor(TimeSlot.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        return mapper;
    }
}
