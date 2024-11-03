package jaeseok.jaeseoklee.dto.user.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgSendSms {
    private List<String> studentNum;
    private String content;
    private String userId;
}