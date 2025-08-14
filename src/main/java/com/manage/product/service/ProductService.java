package com.manage.product.service;

import com.manage.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Page<Product> getProductList(String searchType, String keyword, Pageable pageable);

    Product getProductById(Long id);
    
    void updateProduct(Product product);
    
    void deleteProduct(Long id);
    
    Product saveProduct(Product product, MultipartFile imageFile) throws Exception;
}
