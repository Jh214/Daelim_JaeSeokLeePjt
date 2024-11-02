package jaeseok.jaeseoklee.dto.user.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgSendSmsInfo {
    private String studentName;
    private String studentNum;
}
