package com.manage.benefit.controller;

import com.manage.benefit.entity.Benefit;
import com.manage.benefit.service.BenefitService;
import com.manage.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/benefits")
@RequiredArgsConstructor
public class BenefitController {

    private final BenefitService benefitService;
    
    public BenefitController (BenefitService benefitService) {
    	this.benefitService = benefitService;
    }

    // 모든 혜택 조회
    @GetMapping
    public List<Benefit> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    // 새 혜택 추가
    @PostMapping("/new")
    public Benefit createBenefit(@RequestBody Benefit benefit) {
        return benefitService.saveBenefit(benefit);
    }

    // 혜택 수정
    @PostMapping("/edit")
    public Benefit editBenefit(@RequestBody Benefit benefit) {
        return benefitService.updateBenefit(benefit);
    }

    // 혜택 삭제
    @PostMapping("/delete")
    public void deleteBenefit(@RequestBody Benefit benefit) {
        benefitService.deleteBenefit(benefit.getName());
    }

    // 상품 검색
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return benefitService.searchProductsByName(keyword);
    }
}
