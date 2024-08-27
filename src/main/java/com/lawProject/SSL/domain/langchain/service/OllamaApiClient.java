package com.lawProject.SSL.domain.langchain.service;


import com.lawProject.SSL.domain.langchain.domain.SenderType;
import com.lawProject.SSL.domain.langchain.dto.ChatMessageDto;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaApiClient {
    private final WebClient webClient;
    private final ChatService chatService;
    private final UserService userService;

    /** AI 질의응답 메서드
    반환 타입을 Mono로 감싸는 이유
    비동기 작업을 외부에서 처리하려면 리액티브 타입으로 반환하는 것이 좋다.
    이렇게 하면 호출자가 메서드의 실행이 완료될 때까지 비동기적으로 기다릴 수 있다.
    *  */
    @Transactional
    public Mono<ChatMessageDto.ChatResponse> sendQuestion(String roomId, HttpServletRequest request, String question) {
        User user = userService.getUserInfo(request);
        // 사용자 질문 저장
        chatService.saveMessage(user, question, roomId, SenderType.USER);

        ChatMessageDto.ChatRequest chatRequest = ChatMessageDto.ChatRequest.builder()
                .roomId(roomId)
                .content(question)
                .senderType(SenderType.USER)
                .build();

        return webClient.post()
                .uri("/ai/chat/message")
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatMessageDto.ChatResponse.class);
    }
}
