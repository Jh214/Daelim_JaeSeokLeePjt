package jaeseok.jaeseoklee.repository.websocket;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jaeseok.jaeseoklee.dto.ResponseDto;
import jaeseok.jaeseoklee.dto.websocket.QResponseChat;
import jaeseok.jaeseoklee.dto.websocket.ResponseChat;
import jaeseok.jaeseoklee.entity.websocket.Chat;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jaeseok.jaeseoklee.entity.websocket.QChat.chat;
import static jaeseok.jaeseoklee.entity.websocket.QChatRoom.chatRoom;

@Repository
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private JPAQueryFactory queryFactory;

    //    public ChatRepositoryImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
    @Autowired
    public ChatRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseChat> getChats(Pageable pageable) {
        List<ResponseChat> content = queryFactory
                .select(new QResponseChat(
                        chat.chatId,
                        chatRoom.chatRoomId.as("chatRoomId"),
                        chat.user.uid.as("uid"),
                        chat.message,
                        chat.sendTime,
                        chat.user.userRealName.as("userRealName")
                ))
                .from(chat)
                .leftJoin(chat.chatRoom, chatRoom)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Chat> countQuery = queryFactory
                .selectFrom(chat)
                .leftJoin(chat.chatRoom, chatRoom);

//        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);

    }
}
