package com.lawProject.SSL.domain.inquery.dao;

import com.lawProject.SSL.domain.inquery.model.InQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InQueryRepository extends JpaRepository<InQuery, Long> {
    Page<InQuery> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
