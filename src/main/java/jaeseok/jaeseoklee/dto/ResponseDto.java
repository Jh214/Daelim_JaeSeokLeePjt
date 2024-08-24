package jaeseok.jaeseoklee.dto;

import jaeseok.jaeseoklee.entity.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D> {
    private boolean result;
    private String message;
    private String status;
    private D data;

    public static <D> ResponseDto<D> setSuccess(String message) {
        return ResponseDto.set(true, message, "200", null);
    }

    public static <D> ResponseDto<D> setFailed(String message) {
        return ResponseDto.set(false, message, "400", null);
    }

    public static <D> ResponseDto<D> setSuccessData(String message, D data) {
        return ResponseDto.set(true, message, "200", data);
    }

    public static <D> ResponseDto<D> setFailedData(String message, D data) {
        return ResponseDto.set(false, message, "400", data);
    }
}