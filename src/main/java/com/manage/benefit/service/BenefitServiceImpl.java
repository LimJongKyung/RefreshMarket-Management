package com.manage.benefit.service;

import com.manage.benefit.entity.Benefit;
import com.manage.benefit.repository.BenefitRepository;
import com.manage.product.entity.Product;
import com.manage.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final BenefitRepository benefitRepository;
    private final ProductRepository productRepository;
    
    public BenefitServiceImpl (BenefitRepository benefitRepository, ProductRepository productRepository) {
    	this.benefitRepository = benefitRepository;
    	this.productRepository = productRepository;
    }

    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    @Override
    public Benefit saveBenefit(Benefit benefit) {
        return benefitRepository.save(benefit);
    }

    @Override
    public Benefit updateBenefit(Benefit benefit) {
        // 이름으로 기존 혜택 찾기
        Benefit origin = benefitRepository.findByName(benefit.getName())
                .orElseThrow(() -> new IllegalArgumentException("해당 혜택이 존재하지 않습니다: " + benefit.getName()));
        origin.setDescription(benefit.getDescription());
        return benefitRepository.save(origin);
    }

    @Override
    public void deleteBenefit(String name) {
        Benefit benefit = benefitRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 혜택이 존재하지 않습니다: " + name));
        benefitRepository.delete(benefit);
    }

    @Override
    public Benefit getBenefitByName(String name) {
        return benefitRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 혜택이 존재하지 않습니다: " + name));
    }
    
    @Override
    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    @Override
    public List<String> searchByName(String keyword) {
        return benefitRepository.findByNameContainingIgnoreCase(keyword)
                                .stream()
                                .map(Benefit::getName)
                                .toList();
    }
}