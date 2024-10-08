package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateDto {
    private String userId;
    private String userNum;
}
