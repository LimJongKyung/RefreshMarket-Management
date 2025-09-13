package com.manage.customer.repository;

import com.manage.customer.entity.MemberBenefit;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, String> {

    // 특정 회원의 베네핏 리스트 조회
    List<MemberBenefit> findByMemberId(String memberId);
    
    MemberBenefit findByMemberIdAndBenefitName(String memberId, String benefitName);
    
    // 특정 회원의 혜택 전체 삭제
    @Transactional
    void deleteByMemberId(String memberId);
}
