package jaeseok.jaeseoklee.service.websocket;

import jaeseok.jaeseoklee.dto.websocket.ChatDto;
import jaeseok.jaeseoklee.dto.websocket.RequestChat;
import jaeseok.jaeseoklee.dto.websocket.ResponseChat;
import jaeseok.jaeseoklee.entity.User;
import jaeseok.jaeseoklee.entity.websocket.Chat;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jaeseok.jaeseoklee.repository.UserRepository;
import jaeseok.jaeseoklee.repository.websocket.ChatRepository;
import jaeseok.jaeseoklee.repository.websocket.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository; // UserRepository 주입

    @Transactional
    public void saveMessage(ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getChatRoomId())
                .orElseThrow(() -> new NoSuchElementException("ChatRoom not found with id: " + chatDto.getChatRoomId()));

        User user = userRepository.findByUserId(chatDto.getSenderId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + chatDto.getSenderId()));
        log.info("ChatRoomId: {}", chatDto.getChatRoomId());
        log.info("SenderId: {}", chatDto.getSenderId());

        chatDto.setSendTime(LocalDateTime.now());
        chatDto.setUserRealName(user.getUserRealName());

        Chat chat = chatDto.toEntity(user, chatRoom);
        chatRepository.save(chat);
    }

    public Page<ResponseChat> getChats(Long chatRoomId, RequestChat requestChat) {
        int page = requestChat.getPage();
        int size = requestChat.getSize();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"));
        return chatRepository.getChats(chatRoomId, pageable);
    }
}