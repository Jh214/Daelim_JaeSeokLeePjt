package jaeseok.jaeseoklee.service;

import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.entity.Messages;
import jaeseok.jaeseoklee.repository.MessagesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessagesService {

    private final MessagesRepository messagesRepository;

    public ResponseDto<?> viewer(String userId, String recId) {
        List<Messages> messagesListByUserId = messagesRepository.findByUserIdOrderByTimestampAsc(userId);
        List<Messages> messagesListByRecId = messagesRepository.findByRecIdOrderByTimestampAsc(recId);

        List<Messages> messagesList = new ArrayList<>(messagesListByUserId);
        messagesList.retainAll(messagesListByRecId);

        if (messagesList.isEmpty()) {
            return ResponseDto.setFailed("채팅 내역이 없습니다.");
        }

        return ResponseDto.setSuccessData("채팅 내역을 출력했습니다.", messagesList);
    }
}
