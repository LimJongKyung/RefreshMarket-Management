package com.manage.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.product.entity.ProductDetailImage;

public interface ProductDetailImageRepository extends JpaRepository<ProductDetailImage, Long> {
	List<ProductDetailImage> findAllByProductId(Long productId);
}
