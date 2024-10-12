package jaeseok.jaeseoklee.controller.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.ChatRoomDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChatRoom;
import jaeseok.jaeseoklee.dto.websocket.ResponseChatRoom;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jaeseok.jaeseoklee.service.websocket.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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