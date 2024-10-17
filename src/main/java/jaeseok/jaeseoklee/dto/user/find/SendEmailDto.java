package jaeseok.jaeseoklee.dto.user.find;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SendEmailDto {
    private String userEmail;
    private String userId;
    private int ranCode = (int) (Math.random() * (1000000 - 100000)) + 100000;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);
    private String emailContent;

    public SendEmailDto(String userEmail, String userId, int ranCode, LocalDateTime timeLimit) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.ranCode = ranCode;
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
                "            " + userId + " 님\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px; border-radius: 8px; text-align: center;\">\n" +
                "            <p style=\"color: #000000;\">새로운 비밀번호로 변경하려면 <span style=\"color: #000000;\"></span> 인증코드가 필요합니다.</p>\n" +
                "            <div style=\"text-align: center; margin-bottom: 10px;\">\n" +
                "            </div>\n" +
                "            <div style=\"font-size: 36px; color: #000000; text-align: center; margin: 20px 0; font-weight: bold; border-top: 1.5px solid rgb(230, 230, 248); border-bottom: 1.5px solid rgb(230, 230, 248); padding: 50px\">\n" +
                "                인증번호 : " + ranCode + "\n" +
                "            </div>\n" +
                "            <p style=\"color: #000000;\">\n" +
                "                페이지로 돌아가서 위 코드를 입력하여 인증해주세요.<br>\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}