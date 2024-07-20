package com.lawProject.SSL.domain.langchain.api;

import com.lawProject.SSL.domain.chatmessage.dto.MessageDto;
import com.lawProject.SSL.domain.langchain.service.ChatService;
import com.lawProject.SSL.global.redis.PublishMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LangChainController {

    private final ChatLanguageModel chatLanguageModel;

    private final ChatService chatService;
    private final ChannelTopic topic;

    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate redisTemplate;

    /**
     * llama3에 전달할 질문을 requestParam으로 전달하면 질문에 대한 대답을 반환
     */
    @MessageMapping("/ai/messages/{room-id}") // '/pub/ai/messages/{room-id}' 경로로 보낸 메시지를 처리
    public void modelChat(@DestinationVariable("room-id") String roomId, @Payload MessageDto messageDto) {
        // 사용자가 보낸 메시지를 PublishMessage 객체로 변환하여 Redis에 퍼블리시
        PublishMessage userMessage = new PublishMessage(roomId, messageDto.getSenderId(), messageDto.getContent(), LocalDateTime.now());
        redisTemplate.convertAndSend(topic.getTopic(), userMessage);
        chatService.saveMessage(messageDto, roomId);

        // AI 응답 생성
        String response = chatLanguageModel.generate(messageDto.getContent());

        // AI 응답 메시지 생성
        MessageDto responseMessageDto = new MessageDto(null, response, "AI");
        PublishMessage publishMessage = new PublishMessage(roomId, responseMessageDto.getSenderId(), responseMessageDto.getContent(), LocalDateTime.now());

        log.info("publishMessage: {}", publishMessage.getContent());

        // AI 응답 메시지를 Redis에 퍼블리시
        redisTemplate.convertAndSend(topic.getTopic(), publishMessage);


        chatService.saveMessage(responseMessageDto, roomId);
    }
}