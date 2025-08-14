package com.manage.requests.repository;

import com.manage.requests.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

    // 컨설턴트 이름 또는 이메일로 검색
    Page<Request> findByConsultantNameContainingIgnoreCaseOrConsultantEmailContainingIgnoreCase(String name, String email, Pageable pageable);

}