package jaeseok.jaeseoklee.controller.websocket;

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
@RequestMapping("/api/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final Environment env;

    @PostMapping("/")
    public ResponseEntity<Long> createChatRoom(@RequestBody RequestChatRoom requestChatRoom) {
        Long chatRoomId = chatRoomService.createChatRoom(requestChatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomId);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ResponseChatRoom> getChatRoom(@PathVariable("clubId") Long clubId) {
        ResponseChatRoom responseChatRoom = chatRoomService.getChatRoom(clubId);

        return ResponseEntity.status(HttpStatus.OK).body(responseChatRoom);
    }


    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);

        return ResponseEntity.noContent().build();
    }
}