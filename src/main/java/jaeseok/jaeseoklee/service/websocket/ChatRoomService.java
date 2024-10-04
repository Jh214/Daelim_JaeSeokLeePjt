package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.websocket.RequestChatRoom;
import jaeseok.jaeseoklee.dto.websocket.ResponseChatRoom;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.repository.websocket.ChatRoomRepository;
import jaeseok.jaeseoklee.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createChatRoom(RequestChatRoom requestChatRoom) {
        Optional<User> optionalUser = userRepository.findById(requestChatRoom.getMasterId());
        User user = optionalUser.get();
        ChatRoom result = chatRoomRepository.save(requestChatRoom.toEntity(user));
        return result.getChatRoomId();
    }

    public ResponseChatRoom getChatRoom(Long clubId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByClubId(clubId);

        return ResponseChatRoom.builder().chatRoomList(chatRoomList).build();
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
    }
}
