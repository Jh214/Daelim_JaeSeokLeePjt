package jaeseok.jaeseoklee.controller.websocket;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "채팅방 생성하는 엔드포인트")
    @PostMapping("/createChatRoom")
    public ResponseDto<?> createChatRoom(@RequestBody RequestChatRoom requestChatRoom) {
        ResponseDto<?> result = chatRoomService.createChatRoom(requestChatRoom);

        return result;
    }

    @Tag(name = "현재 로그인 된 userId가 포함된 채팅방 목록을 불러오는 엔드포인트")
    @GetMapping("/chatInventory")
    public ResponseDto<?> getChatRoom(@RequestParam(name = "userId") String userId) {
        ResponseDto<?> result = chatRoomService.getChatRoom(userId);

        return result;
    }

    @Tag(name = "채팅방을 삭제하는 엔드포인트")
    @DeleteMapping("/delete")
    public ResponseDto<?> deleteChatRoom(@RequestParam(name = "chatRoomId") Long chatRoomId) {
        ResponseDto<?> result = chatRoomService.deleteChatRoom(chatRoomId);

        return result;
    }
}