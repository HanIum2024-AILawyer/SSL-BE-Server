package com.lawProject.SSL.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * 수신된 메시지를 AI와 연동하여 응답을 생성
 */
@Component
@Slf4j
public class WebSockChatHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received payload: {}", payload);

        // WebSocketSession의 속성에서 사용자 정보를 가져오기
        Map<String, Object> attributes = session.getAttributes();
        String userId = (String) attributes.get("userId");

        // AI 모델과 연동하여 응답 생성 로직 추가
        String response = generateResponseFromAI(userId, payload);

        TextMessage textMessage = new TextMessage(response);
        session.sendMessage(textMessage);
    }

    private String generateResponseFromAI(String userId, String question) {
        // 여기에 AI 모델과 연동하여 실제 응답을 생성하는 로직 구현
        return "AI 응답 for user " + userId + ": " + question;
    }
}