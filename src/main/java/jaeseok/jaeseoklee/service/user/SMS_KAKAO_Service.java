package jaeseok.jaeseoklee.service.user;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.jwt.JWTVerificationEmailCodeDto;
import jaeseok.jaeseoklee.dto.user.find.SendEmailDto;
import jaeseok.jaeseoklee.dto.user.find.VerificationCodeDto;
import jaeseok.jaeseoklee.dto.user.sms.SendKakao;
import jaeseok.jaeseoklee.dto.user.sms.VerificationSmsCode;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SMS_KAKAO_Service {

    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final String apiUrl = "https://api.coolsms.co.kr/messages/v4/send";

    private final ConcurrentMap<String, SendKakao> codeStore = new ConcurrentHashMap<>();

    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;

    @Value("${coolsms.sender-number}")
    private String senderNumber;

    @Value("${coolsms.pf-id}")
    private String pfId;

    @Value("${coolsms.template-id}")
    private String templateId;

//    알림톡으로 인증코드 보내는 메서드
    public ResponseDto<?> validateAndSendKakao(SendKakao sendKakao) {
        log.info("apiKey = {}, apiSecret = {}, senderNumber = {}, pfId = {}, templateId = {}",
                apiKey, apiSecret, senderNumber, pfId, templateId);

        // 전화번호 유효성 검사
        String userNum = sendKakao.getUserNum();
        if (userNum == null || userNum.isEmpty()) {
            return ResponseDto.setFailed("유효하지 않은 사용자 번호입니다.");
        }

        String formattedNum = formatUserNum(userNum);
        if (userRepository.existsByUserNum(formattedNum)) {
            return ResponseDto.setFailed("이미 등록된 전화번호 입니다.");
        }

        // 랜덤 코드의 유효성 체크
        if (sendKakao.getTimeLimit() == null || sendKakao.getTimeLimit().isBefore(LocalDateTime.now())) {
            return ResponseDto.setFailed("인증 코드의 유효 시간이 만료되었거나 잘못되었습니다.");
        }

        // 카카오톡 메시지 전송
        try {
            String date = OffsetDateTime.now(ZoneOffset.UTC).format(DATE_FORMATTER);
            String salt = generateSalt(sendKakao);
            String signature = createSignature(date, salt);

            HttpHeaders headers = createHeaders(date, salt, signature);
            HttpEntity<Map<String, Object>> request = createRequestEntity(sendKakao, headers);

            // API 호출 및 응답 처리
            ResponseEntity<String> response = new RestTemplate().exchange(apiUrl, HttpMethod.POST, request, String.class);
            return handleResponse(response);
        } catch (Exception e) {
            log.error("Error occurred while sending Kakao message", e);
            return ResponseDto.setFailed("알 수 없는 오류가 발생했습니다.");
        }
    }

//    알림톡으로 받은 인증코드 검증 메서드
    public ResponseDto<?> verificationKakaoCode(VerificationSmsCode verCode) {
        String userNum = verCode.getUserNum();
        int inputCode = verCode.getInputCode();

        SendKakao sendKakao = codeStore.get(userNum);

        if (sendKakao == null) {
            return ResponseDto.setFailed("해당 전화번호로 전송된 인증번호가 존재하지 않습니다.");
        }

        if (LocalDateTime.now().isAfter(sendKakao.getTimeLimit())) {
            codeStore.remove(userNum);  // 5분 지나면 코드 삭제
            return ResponseDto.setFailed("인증 코드가 만료되었습니다. 다시 시도해주세요.");
        }

        if (sendKakao.getRanCode() != inputCode) {
            return ResponseDto.setFailed("인증 코드가 일치하지 않습니다.");
        }

        codeStore.remove(userNum); // 인증 성공 후 코드를 삭제
        return ResponseDto.setSuccess("인증이 성공적으로 완료되었습니다.");
    }


    private String formatUserNum(String userNum) {
        return userNum.replaceAll("^(\\d{3})(\\d{4})(\\d{4})$", "$1-$2-$3");
    }

    private HttpHeaders createHeaders(String date, String salt, String signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", String.format("HMAC-SHA256 apiKey=%s, date=%s, salt=%s, signature=%s",
                apiKey, date, salt, signature));
        return headers;
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(SendKakao sendKakao, HttpHeaders headers) {
        String userNum = sendKakao.getUserNum();
        Map<String, Object> kakaoOptions = new HashMap<>();
        kakaoOptions.put("pfId", pfId);
        kakaoOptions.put("templateId", templateId);
        kakaoOptions.put("variables", createVariables(sendKakao.getRanCode()));

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("to", userNum);
        messageData.put("from", senderNumber);
        messageData.put("type", "ATA");
        messageData.put("kakaoOptions", kakaoOptions);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", messageData);

        SendKakao newSendKakao = new SendKakao(userNum, sendKakao.getRanCode(), sendKakao.getTimeLimit());
        codeStore.put(userNum, newSendKakao);

        return new HttpEntity<>(requestBody, headers);
    }

    private Map<String, String> createVariables(int ranCode) {
        Map<String, String> variables = new HashMap<>();
        variables.put("#{certification}", String.valueOf(ranCode));
        return variables;
    }

    private String createSignature(String date, String salt) throws Exception {
        String message = date + salt;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec signingKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(rawHmac);
    }

    // 바이트 배열을 16진수 문자열로 변환하는 유틸리티 메서드
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String generateSalt(SendKakao sendKakao) {
        int ranCode = sendKakao.getRanCode();
        byte[] saltBytes = new byte[16];

        // ranCode를 사용하여 SecureRandom의 시드를 정합니다.
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(ranCode); // ranCode를 시드로 사용

        // 랜덤 salt를 생성합니다.
        secureRandom.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    private ResponseDto<?> handleResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseDto.setSuccess("메시지가 성공적으로 전송되었습니다.");
        } else {
            String errorMessage = response.getBody();
            log.error("메시지 전송 실패: {}", errorMessage);
            return ResponseDto.setFailed("메시지 전송에 실패했습니다: " + errorMessage);
        }
    }
}
