package com.lawProject.SSL.domain.inquery.service;

import com.lawProject.SSL.domain.inquery.dao.InQueryRepository;
import com.lawProject.SSL.domain.inquery.exception.InQueryException;
import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.domain.user.model.UserRole;
import com.lawProject.SSL.global.common.code.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lawProject.SSL.domain.inquery.dto.InQueryDto.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InQueryService {
    private final InQueryRepository inQueryRepository;

    /* Q&A 작성 메서드 */
    @Transactional
    public void write(User user, InQueryWriteRequest inQueryWriteRequest) {
        InQuery inQuery = inQueryWriteRequest.toEntity(user);
        inQueryRepository.save(inQuery);
        user.addInQuery(inQuery);
        log.info("User ID {}가 새로운 Q&A를 작성했습니다.", user.getId());
    }

    /* Q&A 상세 페이지 메서드 */
    public InQueryDetailResponse getInQueryDetail(Long inQueryId) {
        InQuery inQuery = findInQueryById(inQueryId);
        return InQueryDetailResponse.of(inQuery);
    }

    /* 어드민 Q&A 목록 조회 메서드 */
    public PageImpl<AdminInQueryListResponse> getInQueryList(int page, int size, boolean isAnswered) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InQuery> inQueryPages = inQueryRepository.findAllByIsAnswerOrderByCreatedAtDesc(isAnswered, pageable);

        List<AdminInQueryListResponse> inQueryListResponses = inQueryPages.stream()
                .map(AdminInQueryListResponse::of)
                .toList();

        return new PageImpl<>(inQueryListResponses, pageable, inQueryPages.getTotalElements());
    }


    /* 나의 Q&A 목록 조회 메서드 */
    public List<InQueryListResponse> getMyInQuery(User user, boolean isAnswered) {
        // 매개변수에 따라 필터링
        return user.getInQueryList().stream()
                .filter(i -> i.getIsAnswer() == isAnswered)  // 매개변수에 따라 필터링
                .map(InQueryListResponse::of)
                .toList();
    }

    @Transactional
    /* 문의글 답변 달기 메서드
    * 역할이 Admin 사용자만 가능 */
    public void answer(User admin, @Valid InQueryAnswerRequest inQueryAnswerRequest) {
        // 역할이 Admin이 맞는지 2차 검증
        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new UserException(ErrorCode._FORBIDDEN);
        }

        InQuery inQuery = findInQueryById(inQueryAnswerRequest.id());
        inQuery.setAnswer(inQueryAnswerRequest.answer());
        log.info("InQuery ID {}에 답변을 작성했습니다.", inQuery.getId());
    }

    /* Using Method */
    public InQuery findInQueryById(Long id) {
        return inQueryRepository.findById(id)
                .orElseThrow(() -> new InQueryException(ErrorCode.INQUERY_NOT_FOUND));
    }
}
