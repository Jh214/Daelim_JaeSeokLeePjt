package jaeseok.jaeseoklee.dto.user.find;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
public class FindUserIdSendEmailDto {
    private String emailAddr;
    private String userId;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);
    private String emailContent;

    public FindUserIdSendEmailDto(String emailAddr, String userId, LocalDateTime timeLimit) {
        this.emailAddr = emailAddr;
        this.userId = userId;
        this.timeLimit = timeLimit;
        this.emailContent = generateEmailContent();
    }

    // 이메일 내용 생성 메서드
    private String generateEmailContent() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>TeacHub Email Auth</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #ffffff; color: #000000; margin: 0; padding: 0;\">\n" +
                "    <div style=\"width: 100%; max-width: 600px; margin: 0 auto; padding: 20px;/* background-color: rgb(230, 230, 248);*/ border-radius: 8px; /*border: 1px solid rgb(230, 230, 248);*/\">\n" +
                "        <div style=\"font-size: 24px; color: #000000; margin-bottom: 20px; background-color: rgb(230, 230, 260); padding: 10px\">\n" +
                "            TeacHub 아이디 찾기\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px; border-radius: 8px; text-align: center;\">\n" +
                "            <p style=\"color: #000000;\">회원님의 <span style=\"color: #000000;\"></span> 아이디는 다음과 같습니다.</p>\n" +
                "            <div style=\"text-align: center; margin-bottom: 10px;\">\n" +
                "            </div>\n" +
                "            <div style=\"font-size: 36px; color: #000000; text-align: center; margin: 20px 0; font-weight: bold; border-top: 1.5px solid rgb(230, 230, 248); border-bottom: 1.5px solid rgb(230, 230, 248); padding: 50px\">\n" +
                "                아이디 : " + userId + "\n" +
                "            </div>\n" +
                "            <p style=\"color: #000000;\">\n" +
                "                비밀번호를 잃어버렸다면 페이지로 돌아가 비밀번호 찾기를 진행 후 로그인을 해주세요.<br>\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
