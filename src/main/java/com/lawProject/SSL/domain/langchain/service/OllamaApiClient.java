package com.lawProject.SSL.domain.langchain.service;


import com.lawProject.SSL.domain.langchain.domain.SenderType;
import com.lawProject.SSL.domain.langchain.dto.ChatMessageDto;
import com.lawProject.SSL.domain.langchain.dto.MakeDocForm;
import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import com.lawProject.SSL.domain.lawsuit.service.FileService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.domain.user.service.UserService;
import com.lawProject.SSL.global.common.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OllamaApiClient {
    private final WebClient webClient;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final FileService fileService;
    private final LawSuitRepository lawSuitRepository;

    /** AI 질의응답 메서드
    반환 타입을 Mono로 감싸는 이유
    비동기 작업을 외부에서 처리하려면 리액티브 타입으로 반환하는 것이 좋다.
    이렇게 하면 호출자가 메서드의 실행이 완료될 때까지 비동기적으로 기다릴 수 있다.
    *  */
    @Transactional
    public Mono<ChatMessageDto.ChatResponse> sendQuestion(String roomId, HttpServletRequest request, String question) {
        User user = userService.getUserInfo(request);
        // 사용자 질문 저장
        chatMessageService.saveMessage(user, question, roomId, SenderType.USER);

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

    /* 소송장 첨삭 메서드 */
    @Transactional
    public Mono<FileStorageResult> fixDoc(MultipartFile file, HttpServletRequest request) {

        if (!isWordFile(file)) {
            log.info("파일 형식이 올바르지 않습니다.");
            throw new FileException(ErrorCode.INVALID_FILE_TYPE);
        }

        return webClient.post()
                .uri("/api/doc/fix_doc")
                .bodyValue(file)
                .retrieve()
                .bodyToMono(Resource.class)
                .flatMap(resource -> savedFile(request, resource));
    }

    /* 소송장 생성 메서드 */
    public Mono<FileStorageResult> makeDoc(MakeDocForm makeDocForm, HttpServletRequest request) {
        String docType = makeDocForm.getDoc_type();
        return webClient.post()
                .uri("/ai/doc/make_doc")
                .bodyValue(makeDocForm)
                .retrieve()
                .bodyToMono(Resource.class)
                .flatMap(resource -> savedFileWithType(request, resource, docType));
    }

    /* Using Method */
    // 파일 저장
    private Mono<FileStorageResult> savedFileWithType(HttpServletRequest request, Resource fixedDocResource, String lawSuitType) {
        return Mono.fromCallable(() -> {
            FileStorageResult fileStorageResult = fileService.saveFixedFile(fixedDocResource);
            User user = userService.getUserInfo(request);

            LawSuit lawSuit = LawSuit.ofUserWithType(user, fileStorageResult, lawSuitType);
            lawSuitRepository.save(lawSuit);
            user.addLawsuit(lawSuit);

            return fileStorageResult;
        });
    }

    private Mono<FileStorageResult> savedFile(HttpServletRequest request, Resource fixedDocResource) {
        return Mono.fromCallable(() -> {
            FileStorageResult fileStorageResult = fileService.saveFixedFile(fixedDocResource);
            User user = userService.getUserInfo(request);

            LawSuit lawSuit = LawSuit.ofUser(user, fileStorageResult);
            lawSuitRepository.save(lawSuit);
            user.addLawsuit(lawSuit);

            return fileStorageResult;
        });
    }

    // word 형식이 맞는지 체크
    private boolean isWordFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = getFileExtension(originalFilename);
            return extension != null && (
                    extension.equalsIgnoreCase("doc") ||
                            extension.equalsIgnoreCase("docx")
            );
        }
        return false;
    }

    // 확장자 추출
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }
}
