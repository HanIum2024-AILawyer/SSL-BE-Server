package com.lawProject.SSL.domain.lawsuit.repository;

import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LawSuitRepository extends JpaRepository<LawSuit, Long> {
    Optional<LawSuit> findByStoredFileName(String StoredFileName);

    // 만료 기한이 지난 LawSuit 찾기
    List<LawSuit> findByExpireTimeBefore(LocalDateTime localDateTime);
}
