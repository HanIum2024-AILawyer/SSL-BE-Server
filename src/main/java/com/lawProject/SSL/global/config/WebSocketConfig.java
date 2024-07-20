package com.lawProject.SSL.global.config;

import com.lawProject.SSL.global.security.filter.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    // sockJS Fallback을 이용해 노출할 endpoint 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket이 handshake를 하기 위해 연결하는 endpoint
        registry.addEndpoint("/stomp/chat")
//                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
//                .withSockJS(); // Web Socket을 지원하지 않는 브라우저에서 HTTP의 Polling과 같은 방식으로 WebSocket의 요청을 수행하도록 돕는다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 서버 -> 클라이언트로 발행하는ㅁ ㅔ시지에 대한 endpoint 설정 : 구독
        config.enableSimpleBroker("/sub"); // 클라이언트가 '/sub/**' 경로를 구독하고 있으면, 서버가 '/sub/**'로 메시지를 보내면 브로커가 이를 클라이언트에게 전달

        // 클라이언트 -> 서버로 발행하는 메시지에 대한 endpoint 설정 : 구독에 대한 메시지
        config.setApplicationDestinationPrefixes("/pub"); // '/pub/**' 경로로 메시지를 보내면, 애플리케이션의 메시지 핸들러(@MessageMapping 애너테이션이 붙은 메서드)에서 처리하도록 함
    }
}
