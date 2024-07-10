package com.lawProject.SSL.domain.lawsuit.repository;

import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LawSuitRepository extends JpaRepository<LawSuit, Long> {
}
