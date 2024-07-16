package com.lawProject.SSL.global.config;

import com.lawProject.SSL.global.handler.WebSockChatHandler;
import com.lawProject.SSL.global.security.filter.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSockChatHandler webSockChatHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(WebSockChatHandler webSockChatHandler, JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.webSockChatHandler = webSockChatHandler;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSockChatHandler, "/ws/chat")
                .addInterceptors(jwtHandshakeInterceptor)  // JWT 인증 인터셉터 추가
                .setAllowedOrigins("*");
    }
}
