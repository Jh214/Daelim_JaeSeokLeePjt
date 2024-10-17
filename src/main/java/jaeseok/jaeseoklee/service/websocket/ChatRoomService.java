package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChatRoom;
import jaeseok.jaeseoklee.dto.websocket.ResponseChatRoom;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.repository.websocket.ChatRoomRepository;
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
    public ResponseDto<?> createChatRoom(RequestChatRoom requestChatRoom) {
        Optional<User> optionalMasterUser = userRepository.findByUserId(requestChatRoom.getMasterId());
        Optional<User> optionalParticipant = userRepository.findByUserId(requestChatRoom.getParticipantId());

        if (!optionalMasterUser.isPresent()) {
            return ResponseDto.setFailed("현재 로그인 세션이 잘못되었습니다.");
        }
        if (!optionalParticipant.isPresent()) {
            return ResponseDto.setFailed("존재하지 않는 회원입니다.");
        }

        User creator = optionalMasterUser.get();
        User participant = optionalParticipant.get();
        chatRoomRepository.save(requestChatRoom.toEntity(creator, participant));

        return ResponseDto.setSuccess("채팅방이 생성되었습니다.");
    }

    public ResponseDto<?> getChatRoom(String userId) {
        // masterId로 로그인된 유저가 소속된 채팅방 목록 조회
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);

        // 조회된 채팅방 목록을 ResponseChatRoom 객체로 변환
        ResponseChatRoom responseChatRoom = ResponseChatRoom.builder()
                .chatRoomList(chatRoomList)
                .build();

        return ResponseDto.setSuccessData("채팅방 목록을 성공적으로 불러왔습니다.", responseChatRoom);
    }


    @Transactional
    public ResponseDto<?> deleteChatRoom(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
        return ResponseDto.setSuccess("채팅방이 삭제되었습니다");
    }
}
