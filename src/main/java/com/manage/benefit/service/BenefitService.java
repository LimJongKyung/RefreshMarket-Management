package com.manage.benefit.service;

import com.manage.benefit.entity.Benefit;
import com.manage.product.entity.Product;

import java.util.List;

public interface BenefitService {
    List<Benefit> getAllBenefits();
    Benefit saveBenefit(Benefit benefit);
    Benefit updateBenefit(Benefit benefit);
    void deleteBenefit(String name);
    Benefit getBenefitByName(String name);
    
    List<Product> searchProductsByName(String keyword); // 혜택을 줄 상품 검색
    
    List<String> searchByName(String keyword);
}