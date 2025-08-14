package com.manage.product.repository;

import com.manage.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 검색 조건: 이름, 설명, 카테고리 ID (String 형태)
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
}