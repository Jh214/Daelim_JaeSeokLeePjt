package jaeseok.jaeseoklee.controller.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatService messagesService;

    @GetMapping("/view")
    public ResponseDto<?> messageView(@RequestParam(name = "userId") String userId, @RequestParam(name = "recId") String recId) {
        ResponseDto<?> result = messagesService.viewer(userId, recId);

        return result;
    }
}