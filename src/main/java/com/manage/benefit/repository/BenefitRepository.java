package com.manage.benefit.repository;

import com.manage.benefit.entity.Benefit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitRepository extends JpaRepository<Benefit, String> {
	Optional<Benefit> findByName(String name);
	
	List<Benefit> findByNameContainingIgnoreCase(String name); // 이름으로 검색
}