package com.lawProject.SSL.global.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String message) {
        try {
            log.info("publish 전 message: {}", message);
            PublishMessage publishMessage = objectMapper.readValue(message, PublishMessage.class);

            messagingTemplate.convertAndSend("/sub/chats/" + publishMessage.getRoomId(), publishMessage); // roomId를 사용하여 적절한 STOMP 브로커 주제로 메시지를 전송한다.
            log.info("publish 후 message: {}", publishMessage.getContent());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}