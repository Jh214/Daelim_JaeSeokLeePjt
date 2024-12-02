package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.ChatRoomDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseDto<?> createChatRoom(RequestChatRoom requestChatRoom) {
        Optional<User> optionalMasterUser = userRepository.findByUserId(requestChatRoom.getMasterId());
        Optional<User> optionalParticipant = userRepository.findByUserId(requestChatRoom.getParticipantId());

        if (!optionalMasterUser.isPresent()) {
            return ResponseDto.setFailed("현재 로그인 세션이 잘못되었습니다.");
        }
        if (!optionalParticipant.isPresent()) {
            return ResponseDto.setFailed("존재하지 않는 회입니다.");
        }

        User creator = optionalMasterUser.get();
        User participant = optionalParticipant.get();
        chatRoomRepository.save(requestChatRoom.toEntity(creator, participant));

        return ResponseDto.setSuccess("채팅방이 생성되었습니다.");
    }

    @Override
    public ResponseDto<?> getChatRoom(String userId) {
        try {
            // userId에 해당하는 채팅방 목록 조회
            List<ChatRoom> chatRoomList = findChatRoomsByUserId(userId);

            if (chatRoomList.isEmpty()) {
                return ResponseDto.setFailed("해당 유저의 채팅방이 존재하지 않습니다.");
            }

            // ChatRoom 목록을 ChatRoomDto 목록으로 변환
            List<ChatRoomDto> chatRoomDtoList = convertToChatRoomDtoList(chatRoomList, userId);

            // ResponseChatRoom에 ChatRoomDto 리스트 전달
            ResponseChatRoom responseChatRoom = new ResponseChatRoom(chatRoomDtoList);

            return ResponseDto.setSuccessData("채팅방 목록을 성공적으로 불러왔습니다.", responseChatRoom);
        } catch (Exception e) {
            // 일반적인 예외
            return ResponseDto.setFailed("채팅방 목록을 불러오는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // userId로 채팅방 목록을 조회하는 메서드
    private List<ChatRoom> findChatRoomsByUserId(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);
        if (chatRooms == null) {
            throw new NullPointerException("채팅방 목록을 가져오는 데 실패했습니다.");
        }
        return chatRooms;
    }

    // 각 ChatRoom 을 ChatRoomDto 로 변환하는 메서드
    private List<ChatRoomDto> convertToChatRoomDtoList(List<ChatRoom> chatRoomList, String userId) {
        return chatRoomList.stream().map(chatRoom -> {
            String counterpartName = getCounterpartName(chatRoom, userId);

            // 마지막 메시지 시간 가져오기
            String lastMessageTime = (chatRoom.getLatestMessage() != null && chatRoom.getLatestMessage().getSendTime() != null)
                    ? chatRoom.getLatestMessage().getSendTime().toString()
                    : "";

            return ChatRoomDto.builder()
                    .chatRoomId(chatRoom.getChatRoomId())
                    .masterId(chatRoom.getCreator().getUserId())
                    .participantId(chatRoom.getParticipant().getUserId())
                    .name(counterpartName)  // 상대방 이름으로 설정
                    .lastMessages(chatRoom.getLatestMessage() != null ? chatRoom.getLatestMessage().getMessage() : "")
                    .lastMessagesTime(lastMessageTime)  // 마지막 메시지 시간을 설정
                    .build();
        }).collect(Collectors.toList());
    }

    // 상대방의 이름을 가져오는 메서드
    private String getCounterpartName(ChatRoom chatRoom, String userId) {
        if (chatRoom == null || chatRoom.getCreator() == null || chatRoom.getParticipant() == null) {
            throw new NullPointerException("유효하지 않은 채팅방 데이터입니다.");
        }
        if (userId.equals(chatRoom.getCreator().getUserId())) {
            // 로그인 유저가 masterId인 경우, 상대방 이름은 participantName
            return chatRoom.getParticipant().getUserRealName();
        } else if (userId.equals(chatRoom.getParticipant().getUserId())) {
            // 로그인 유저가 participantId인 경우, 상대방 이름은 masterName
            return chatRoom.getCreator().getUserRealName();
        } else {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.");
        }
    }


    @Transactional
    @Override
    public ResponseDto<?> deleteChatRoom(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
        return ResponseDto.setSuccess("채팅방이 삭제되었습니다");
    }
}
