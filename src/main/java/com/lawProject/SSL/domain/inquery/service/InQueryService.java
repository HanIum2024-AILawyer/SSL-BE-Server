package com.lawProject.SSL.domain.inquery.service;

import com.lawProject.SSL.domain.inquery.dao.InQueryRepository;
import com.lawProject.SSL.domain.inquery.exception.InQueryException;
import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
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
    private InQueryRepository inQueryRepository;
    private UserService userService;

    /* Q&A 작성 메서드 */
    @Transactional
    public void write(HttpServletRequest request, InQueryWriteRequest inQueryWriteRequest) {
        User user = userService.getUserInfo(request);
        InQuery inQuery = inQueryWriteRequest.toEntity(user);
        inQueryRepository.save(inQuery);
        user.addInQuery(inQuery);
    }

    /* Q&A 상세 페이지 메서드 */
    public InQueryDetailResponse getInQueryDetail(Long inQueryId) {
        InQuery inQuery = findInQueryById(inQueryId);
        return InQueryDetailResponse.of(inQuery);
    }

    /* Q&A 목록 조회 메서드 */
    public PageImpl<InQueryListResponse> getInQueryList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InQuery> inQueryPages = inQueryRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<InQueryListResponse> inQueryListResponses = inQueryPages.stream()
                .map(InQueryListResponse::of)
                .toList();

        return new PageImpl<>(inQueryListResponses, pageable, inQueryPages.getTotalElements());
    }

    /* Using Method */
    public InQuery findInQueryById(Long id) {
        return inQueryRepository.findById(id)
                .orElseThrow(() -> new InQueryException(ErrorCode.INQUERY_NOT_FOUND));
    }
}
