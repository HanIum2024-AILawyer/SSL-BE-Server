package com.lawProject.SSL.global.config;

import com.lawProject.SSL.global.handler.WebSockChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSockChatHandler webSockChatHandler;

    public WebSocketConfig(WebSockChatHandler webSockChatHandler) {
        this.webSockChatHandler = webSockChatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSockChatHandler, "/chat")
                .setAllowedOrigins("*");
    }
}
