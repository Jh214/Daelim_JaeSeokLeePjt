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

// Jackson 설정 파일 (JSON 데이터 처리하는 곳)
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        /**
         * Enum 타입의 필드에 빈 문자열("")이 들어오면 null로 변환
         * Grade와 DayOfWeek 열거형 타입 필드에 빈 문자열이 들어올 경우 오류를 방지하고 null로 처리합니다.
         */
        mapper.coercionConfigFor(Grade.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        mapper.coercionConfigFor(DayOfWeek.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        /**
         * 빈 배열([])을 String 필드에 매핑할 경우 null로 처리
         * 예를 들어, 빈 JSON 배열이 String 필드에 매핑되었을 때 오류 없이 null로 변환되도록 설정합니다.
         * */
        mapper.coercionConfigFor(String.class)
                .setCoercion(CoercionInputShape.Array, CoercionAction.AsNull);

        /**
         * 비어 있는 Array나 List를 Null로 처리
         * 빈 배열을 전달받았을 때 이를 null로 간주하여 처리하는 데 도움을 줍니다.
         */
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

        /**
         * 단일 값이 배열 필드로 들어올 때 자동으로 배열로 변환
         * 단일 값이 배열 필드로 들어올 경우, 배열로 자동 변환하여 예외를 방지합니다.
         */
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        /**
         * JavaTimeModule을 등록하여 날짜/시간 처리 지원
         * LocalDate, LocalDateTime 같은 Java 시간 관련 클래스를 Jackson이 올바르게 변환할 수 있도록 모듈을 등록합니다.
         */
        mapper.registerModule(new JavaTimeModule());

        /**
         * 빈 배열을 List 필드에 매핑할 때 null로 변환
         * List 타입 필드에 빈 배열이 매핑될 경우, null로 처리되도록 설정합니다.
         */
        mapper.coercionConfigFor(List.class)
                .setCoercion(CoercionInputShape.EmptyArray, CoercionAction.AsNull);

        return mapper;
    }
}