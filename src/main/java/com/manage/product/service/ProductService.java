package com.manage.product.service;

import com.manage.product.entity.Product;
import com.manage.product.entity.ProductDetailImage;
import com.manage.product.entity.ProductImage;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Page<Product> getProductList(String searchType, String keyword, Pageable pageable);

    Product getProductById(Long id);
    
    List<ProductImage> getImagesByProductId(Long productId);
    
    void updateProduct(Product product,
            MultipartFile imageFile,
            List<MultipartFile> extraImages,
            List<MultipartFile> detailImages,
            List<Long> deleteImageIds,
            List<Long> deleteDetailImageIds,  // <- 새로 추가
            String mainDisplay,
            String detailOption,
            String detailOptionPrice) throws IOException;
    
    void deleteProduct(Long id);
    
    void saveProduct(Product product, MultipartFile mainImage, String mainImageType,
            List<MultipartFile> additionalImages, List<String> additionalImageTypes,
            List<MultipartFile> detailImages, List<String> detailImageTypes) throws IOException;
    
    List<ProductImage> getExtraImagesByProductId(Long productId);

	List<ProductDetailImage> getDetailImagesByProductId(Long id);
}
