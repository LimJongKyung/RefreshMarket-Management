package com.manage.product.service;

import com.manage.product.entity.Product;
import com.manage.product.entity.ProductDetailImage;
import com.manage.product.entity.ProductImage;
import com.manage.product.repository.ProductDetailImageRepository;
import com.manage.product.repository.ProductImageRepository;
import com.manage.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository; // 추가
    private final ProductDetailImageRepository productDetailImageRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductImageRepository productImageRepository,
                              ProductDetailImageRepository productDetailImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productDetailImageRepository = productDetailImageRepository;
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
    
    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
    
    @Override
    @Transactional
    public void updateProduct(Product product,
                              MultipartFile imageFile,
                              List<MultipartFile> extraImages,
                              List<MultipartFile> detailImages,
                              List<Long> deleteImageIds,
                              List<Long> deleteDetailImageIds,  // <- 새로 추가
                              String mainDisplay,
                              String detailOption,
                              String detailOptionPrice) throws IOException {

        // 1. 기존 제품 조회
        Product origin = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 제품이 없습니다."));

        // 2. 기본 정보 업데이트
        origin.setName(product.getName());
        origin.setPrice(product.getPrice());
        origin.setDescription(product.getDescription());
        origin.setCategory(product.getCategory());
        origin.setMainDisplay(mainDisplay);
        origin.setDetailOption(detailOption);
        origin.setDetailOptionPrice(detailOptionPrice);

        // 3. 메인 이미지 업데이트
        if (imageFile != null && !imageFile.isEmpty()) {
            origin.setImageData(imageFile.getBytes());
            origin.setImageType("MAIN");
        }

        productRepository.save(origin);

        // 4. 삭제할 추가 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long id : deleteImageIds) {
                productImageRepository.deleteById(id);
            }
        }

        // 5. 삭제할 상세페이지 이미지 삭제 <- 여기 추가
        if (deleteDetailImageIds != null && !deleteDetailImageIds.isEmpty()) {
            for (Long id : deleteDetailImageIds) {
                productDetailImageRepository.deleteById(id);
            }
        }

        // 6. 추가 이미지 저장 (기존)
        if (extraImages != null && !extraImages.isEmpty()) {
            for (MultipartFile file : extraImages) {
                if (!file.isEmpty()) {
                    ProductImage additional = new ProductImage();
                    additional.setProductId(origin.getProductId());
                    additional.setImage(file.getBytes());
                    additional.setImageType("ADDITIONAL");
                    productImageRepository.save(additional);
                }
            }
        }

        // 7. 상세페이지 이미지 저장 (추가)
        if (detailImages != null && !detailImages.isEmpty()) {
            for (MultipartFile file : detailImages) {
                if (!file.isEmpty()) {
                    ProductDetailImage detailImg = new ProductDetailImage();
                    detailImg.setProductId(origin.getProductId());
                    detailImg.setImage(file.getBytes());
                    detailImg.setImageType("DETAIL");
                    productDetailImageRepository.save(detailImg);
                }
            }
        }
    }


    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // 1. 해당 상품의 추가 이미지 삭제
        List<ProductImage> extraImages = productImageRepository.findByProductId(id);
        if (extraImages != null && !extraImages.isEmpty()) {
            productImageRepository.deleteAll(extraImages);
        }

        // 2. 상품 삭제
        productRepository.deleteById(id);
    }
    
    @Transactional
    public void saveProduct(
            Product product,
            MultipartFile mainImage,
            String mainImageType,
            List<MultipartFile> additionalImages,
            List<String> additionalImageTypes,
            List<MultipartFile> detailImages,
            List<String> detailImageTypes) throws IOException {

        // 1. 메인 이미지 Products 테이블에 저장
        if (mainImage != null && !mainImage.isEmpty()) {
            product.setImageData(mainImage.getBytes());
            product.setImageType(mainImageType != null ? mainImageType : "MAIN");
        }

        productRepository.save(product); // 여기서 productId 생성됨

        Long productId = product.getProductId();

        // 2. 추가 이미지 ProductImage 테이블에 저장
        if (additionalImages != null && !additionalImages.isEmpty()) {
            for (int i = 0; i < additionalImages.size(); i++) {
                MultipartFile file = additionalImages.get(i);
                if (!file.isEmpty()) {
                    ProductImage addImg = new ProductImage();
                    addImg.setProductId(productId);
                    addImg.setImage(file.getBytes());
                    addImg.setImageType(
                            (additionalImageTypes != null && additionalImageTypes.size() > i)
                            ? additionalImageTypes.get(i)
                            : "ADDITIONAL"
                    );
                    productImageRepository.save(addImg);
                }
            }
        }

        // 3. 상세 이미지 ProductDetailImage 테이블에 저장
        if (detailImages != null && !detailImages.isEmpty()) {
            for (int i = 0; i < detailImages.size(); i++) {
                MultipartFile file = detailImages.get(i);
                if (!file.isEmpty()) {
                    ProductDetailImage detailImg = new ProductDetailImage();
                    detailImg.setProductId(productId);
                    detailImg.setImage(file.getBytes());
                    detailImg.setImageType(
                            (detailImageTypes != null && detailImageTypes.size() > i)
                            ? detailImageTypes.get(i)
                            : "DETAIL"
                    );
                    productDetailImageRepository.save(detailImg); // 오류 없어야 함
                }
            }
        }
    }
    
    public List<ProductImage> getExtraImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
    
    @Override
    public List<ProductDetailImage> getDetailImagesByProductId(Long productId) {
        return productDetailImageRepository.findAllByProductId(productId);
    }
}