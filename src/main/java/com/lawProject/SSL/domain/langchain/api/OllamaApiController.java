package com.lawProject.SSL.domain.langchain.api;

import com.lawProject.SSL.domain.langchain.dto.MakeDocForm;
import com.lawProject.SSL.domain.langchain.service.OllamaApiClient;
import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import com.lawProject.SSL.domain.lawsuit.model.LawsuitType;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
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
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ollama")
public class OllamaApiController {
    private final OllamaApiClient ollamaApiClient;
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
    @PostMapping("/doc/fix")
    public Mono<ResponseEntity<ApiResponse<FileStorageResult>>> fixDoc(@RequestPart(name = "file") MultipartFile file, HttpServletRequest request) {

        return ollamaApiClient.fixDoc(file, request)
                .map(fileStorageResult -> ApiResponse.onSuccess(SuccessCode._OK, fileStorageResult))
                .onErrorResume(e -> {
                    log.error("Failed to fix document: ", e);
                    return Mono.just(ApiResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR));
                });
    }

    /**
     * 소송장 생성
     */
    @PostMapping("/doc/make")
    public Mono<ResponseEntity<ApiResponse<FileStorageResult>>> makeDoc(@RequestBody MakeDocForm makeDocForm, HttpServletRequest request) {
        // 소송 유형에 따라 입력값 검증
        if (makeDocForm.getLawsuitType() == LawsuitType.CIVIL) {
            if (makeDocForm.getClaimAmount() == null) {
                return Mono.just(ApiResponse.onFailure(ErrorCode._INVALID_INPUT_VALUE));
            }
        } else if (makeDocForm.getLawsuitType() == LawsuitType.CRIMINAL) {
            if (makeDocForm.getDamageScale() == null) {
                return Mono.just(ApiResponse.onFailure(ErrorCode._INVALID_INPUT_VALUE));
            }
        } else {
            return Mono.just(ApiResponse.onFailure(ErrorCode._INVALID_INPUT_VALUE));
        }

        return ollamaApiClient.makeDoc(makeDocForm, request)
                .map(fileStorageResult -> ApiResponse.onSuccess(SuccessCode._OK, fileStorageResult))
                .onErrorResume(e -> {
                    log.error("Failed to create document: ", e);
                    return Mono.just(ApiResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR));
                });
    }

}