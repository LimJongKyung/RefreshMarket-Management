package com.manage.product.service;

import com.manage.product.entity.Product;
import com.manage.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 리스트
    @Override
    public Page<Product> getProductList(String searchType, String keyword, Pageable pageable) {
        if (searchType == null || keyword == null || keyword.isBlank()) {
            return productRepository.findAll(pageable);
        }

        switch (searchType) {
            case "name":
                return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
            case "description":
                return productRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
            case "category":
                return productRepository.findByCategory(keyword, pageable);  // ✅ 바로 사용
            default:
                return productRepository.findAll(pageable);
        }
    }

    // 상세
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. ID: " + id));
    }
    
    // 제품 수정
    @Override
    public void updateProduct(Product product) {
        // 기존 엔티티 조회 후 수정하는 방법 (권장)
        Product origin = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 제품이 없습니다."));

        origin.setName(product.getName());
        origin.setDescription(product.getDescription());
        origin.setPrice(product.getPrice());
        origin.setStock(product.getStock());
        origin.setCategory(product.getCategory());
        origin.setImageData(product.getImageData()); // 이미지 바이트 배열 저장
        origin.setMainDisplay(product.getMainDisplay());
        origin.setSeller(product.getSeller());
        origin.setManufacturer(product.getManufacturer());
        origin.setDetailOption(product.getDetailOption());
        
        productRepository.save(origin);
    }
    
    // 삭제
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // 제품 등록
    @Override
    public Product saveProduct(Product product, MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setImageData(imageFile.getBytes());
            product.setImageType(imageFile.getContentType());
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }
}