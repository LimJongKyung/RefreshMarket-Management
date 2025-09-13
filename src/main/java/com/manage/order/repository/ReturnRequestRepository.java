package com.manage.order.repository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manage.order.entity.ReturnRequest;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
	@Query("SELECT r FROM ReturnRequest r WHERE " +
	           "LOWER(r.memberId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(r.productId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(r.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	    Page<ReturnRequest> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
