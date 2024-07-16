package com.lawProject.SSL.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class WebSockChatHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received payload: {}", payload);

        // AI 모델과 연동하여 응답 생성 로직 추가
        String response = generateResponseFromAI(payload);

        TextMessage textMessage = new TextMessage(response);
        session.sendMessage(textMessage);
    }

    private String generateResponseFromAI(String question) {
        // 여기에 AI 모델과 연동하여 실제 응답을 생성하는 로직 구현
        return "AI 응답: " + question;
    }
}