package jaeseok.jaeseoklee.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
public class SendEmailDto {
    private String emailAddr;
    private String userId;
    private int ranCode = (int) (Math.random() * (1000000 - 100000)) + 100000;
    private LocalDateTime timeLimit = LocalDateTime.now().plusMinutes(5);
    private String emailContent;

    public SendEmailDto(String emailAddr, String userId, int ranCode, LocalDateTime timeLimit) {
        this.emailAddr = emailAddr;
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
                "<body style=\"font-family: Arial, sans-serif; background-color: #1b1b1b; color: #cccccc; margin: 0; padding: 0;\">\n" +
                "    <div style=\"width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #2a2a2a; border-radius: 8px;\">\n" +
                "        <div style=\"font-size: 24px; color: #ffffff; margin-bottom: 20px;\">\n" +
                "            " + userId + " 님,\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #2a2a2a; padding: 20px; border-radius: 8px;\">\n" +
                "            <p style=\"color: #ffffff;\">새로운 비밀번호로 변경하려면 <span style=\"color: #f0c674;\"></span> 코드가 필요합니다.</p>\n" +
                "            <div style=\"text-align: center; margin-bottom: 10px;\">\n" +
                "            </div>\n" +
                "            <div style=\"font-size: 36px; color: #60aee5; text-align: center; margin: 20px 0; font-weight: bold;\">\n" +
                "                " + ranCode + "\n" +
                "            </div>\n" +
                "            <p style=\"color: #ffffff;\">\n" +
                "                페이지로 돌아가서 위 코드를 입력하여 인증하세요.<br>\n" +
                "            </p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}