package jaeseok.jaeseoklee.config;

//import jaeseok.jaeseoklee.controller.websocket.StompHandler;
import jaeseok.jaeseoklee.controller.websocket.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

//웹소켓 설정파일
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    private final StompHandler stompHandler; // jwt 인증

    // 웹소켓 configuration의 addHandler 메소드와 유사
    // cors, SockJS 설정 가능
    /*
       STOMP를 사용하면 웹소켓만 사용할 때와 다르게 하나의 연결주소마다 핸들러 클래스를 따로 구현할 필요없이
       Controller 방식으로 간편하게 사용할 수 있다.
     */

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 주소 url => /ws
        registry.addEndpoint("/ws") // 연결될 엔드포인트
                .setAllowedOrigins("http://localhost:3000", "https://jsl.comon.kr", "https://jsl2.comon.kr", "http://121.139.20.242:3000", "http://49.163.165.16:3000", "http://121.139.20.242:8859")
                .withSockJS(); // SocketJS 를 연결한다는 설정
    }

    // STOMP에서 사용하는 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 송신
        registry.setApplicationDestinationPrefixes("/app");
        // 수신
        registry.enableSimpleBroker("/topic");
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }
}