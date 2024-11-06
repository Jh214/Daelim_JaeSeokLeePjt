package jaeseok.jaeseoklee.controller.websocket;

import io.swagger.v3.oas.annotations.tags.Tag;
import jaeseok.jaeseoklee.dto.websocket.ChatDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChat;
import jaeseok.jaeseoklee.dto.websocket.ResponseChat;
import jaeseok.jaeseoklee.service.websocket.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    // 메시지 브로커와 상호작용하여 WebSocket 메시지를 전송하는 데 사용
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // /chat/send 엔드포인트로 들어오는 WebSocket 메시지를 처리한다.
    @Tag(name = "실시간 채팅 ")
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatDto chat) {
        log.info("클라이언트로부터 메시지 수신: {}", chat);
        // 채팅 저장
        chatService.saveMessage(chat);
        System.out.println(chat.getSenderId());
        // 해당 채팅 메시지를 WebSocket 토픽(/topic/채팅방ID)에 전송하여 클라이언트에게 브로드캐스팅한다.
        messagingTemplate.convertAndSend("/topic/" + chat.getChatRoomId(), chat);
    }

    @Tag(name = "채팅 내역 불러오는 엔드포인트", description = "웹소켓의 순기능과는 거리가 있고 단순하게 채팅 내역만을 불러옴")
    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<Page<ResponseChat>> getChats(@PathVariable(name = "chatRoomId") Long chatRoomId,
                                                       @ModelAttribute RequestChat requestChat) {
        Page<ResponseChat> chatPage = chatService.getChats(chatRoomId, requestChat);

        return ResponseEntity.status(HttpStatus.OK).body(chatPage);
    }
}