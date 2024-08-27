package com.lawProject.SSL.domain.langchain.api;

import com.lawProject.SSL.domain.langchain.service.ChatService;
import com.lawProject.SSL.domain.langchain.service.OllamaApiClient;
import com.lawProject.SSL.global.redis.PublishMessage;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ollama")
public class OllamaApiController {
    private final OllamaApiClient ollamaApiClient;
    private final ChatService chatService;
    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    /**
     * AI 질의응답
     */
    @PostMapping("/question/{roomId}")
    public Mono<ResponseEntity<String>> sendMessage(@PathVariable String roomId,
                                                    HttpServletRequest request,
                                                    @RequestParam("question") String question) {

        return ollamaApiClient.sendQuestion(roomId, request, question)
                .doOnSuccess(response -> {
                    // AI의 응답을 Redis에 퍼블리시
                    PublishMessage publishMessage = new PublishMessage(roomId, response.content(), LocalDateTime.now());

                    log.info("publishMessage: {}", publishMessage.getContent());
                    redisTemplate.convertAndSend(topic.getTopic(), publishMessage);
                })
                .then(Mono.just(ResponseEntity.ok("AI Response Success")))
                .onErrorResume(error -> {
                    log.error("Error while sending question: ", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send question."));
                });
    }

    /**
     * 소송장 첨삭
     */

    /**
     * 소송장 생성
     */


    }