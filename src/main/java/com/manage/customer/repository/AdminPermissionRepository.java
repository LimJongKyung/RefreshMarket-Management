package com.manage.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.customer.entity.AdminPermission;

public interface AdminPermissionRepository extends JpaRepository<AdminPermission, Long> {
	AdminPermission findByMemberId(String memberId);
	
	// 추가 메서드: 특정 회원의 모든 권한 조회
    List<AdminPermission> findAllByMemberId(String memberId);

    // 추가 메서드: 특정 회원이 특정 권한을 가지고 있는지 확인
    boolean existsByMemberIdAndPermName(String memberId, String permName);
}
