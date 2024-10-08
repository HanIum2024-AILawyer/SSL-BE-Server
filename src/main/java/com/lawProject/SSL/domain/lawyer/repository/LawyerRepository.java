package com.lawProject.SSL.domain.lawyer.repository;

import com.lawProject.SSL.domain.lawyer.dto.LawyerDto;
import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//repository, 인터페이스, JpaRepository 사용
/**
 * Spring Data JPA
 */
public interface LawyerRepository extends JpaRepository<Lawyer, Long> {
    /* interface이기 때문에 코드를 작성하지 않아도 JpaRepository(Spring Data JPA)의 기능을 사용 가능
     * 사용가능한 기본적인 코드: 저장(save), 조회(findById), 전체 조회(findByAll), 삭제(deleteById) 등 제공
     * */

    /* Id가 아닌 name으로 조회하고 싶다?
     * 일정한 규칙에 의해서 작성하면 됨*/
    Lawyer findByName(String name); // 이름으로 변호사 조회 기능 개발 끝!
    /* like 검색을 쓰기위해서 사용*/
    List<Lawyer> findByNameContains(String keyword);
}

//기본코드
//변동사항 X