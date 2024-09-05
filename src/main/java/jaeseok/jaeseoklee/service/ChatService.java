package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.entity.Chat;
import jaeseok.jaeseoklee.repository.MessagesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final MessagesRepository messagesRepository;

    public ResponseDto<?> viewer(String userId, String recId) {
        List<Chat> messagesListByUserId = messagesRepository.findByUserIdOrderByTimestampAsc(userId);
        List<Chat> messagesListByRecId = messagesRepository.findByRecIdOrderByTimestampAsc(recId);

        List<Chat> messagesList = new ArrayList<>(messagesListByUserId);
        messagesList.retainAll(messagesListByRecId);

        if (messagesList.isEmpty()) {
            return ResponseDto.setFailed("채팅 내역이 없습니다.");
        }

        return ResponseDto.setSuccessData("채팅 내역을 출력했습니다.", messagesList);
    }
}
