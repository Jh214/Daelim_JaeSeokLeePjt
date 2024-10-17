package jaeseok.jaeseoklee.controller.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChatRoom;
import jaeseok.jaeseoklee.service.websocket.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final Environment env;

    @PostMapping("/createChatRoom")
    public ResponseDto<?> createChatRoom(@RequestBody RequestChatRoom requestChatRoom) {
        ResponseDto<?> result = chatRoomService.createChatRoom(requestChatRoom);

        return result;
    }

    @GetMapping("/chatInventory")
    public ResponseDto<?> getChatRoom(@RequestParam(name = "userId") String userId) {
        ResponseDto<?> result = chatRoomService.getChatRoom(userId);

        return result;
    }

    @DeleteMapping("/delete")
    public ResponseDto<?> deleteChatRoom(@RequestParam(name = "chatRoomId") Long chatRoomId) {
        ResponseDto<?> result = chatRoomService.deleteChatRoom(chatRoomId);

        return result;
    }
}