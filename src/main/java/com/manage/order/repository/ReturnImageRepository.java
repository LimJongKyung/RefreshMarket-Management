package com.manage.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.order.entity.ReturnImage;

public interface ReturnImageRepository extends JpaRepository<ReturnImage, Long> {
    List<ReturnImage> findByReturnRequest_ReturnId(Long returnId);
}