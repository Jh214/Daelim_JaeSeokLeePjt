package jaeseok.jaeseoklee.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jaeseok.jaeseoklee.entity.schedule.DayOfWeek;
import jaeseok.jaeseoklee.entity.Grade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

        // 빈 배열을 Null로 처리
        mapper.coercionConfigFor(String.class)
                .setCoercion(CoercionInputShape.Array, CoercionAction.AsNull);

        // 필요에 따라, 빈 리스트 또는 Array를 Null로 처리
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // JavaTimeModule을 등록하여 날짜/시간 처리
        mapper.registerModule(new JavaTimeModule());

        mapper.coercionConfigFor(List.class)
                .setCoercion(CoercionInputShape.EmptyArray, CoercionAction.AsNull);

        return mapper;
    }
}
