package com.manage.customer.service;

import com.manage.benefit.entity.Benefit;
import com.manage.customer.entity.Customer;
import com.manage.customer.entity.GenderAgeOrderDTO;
import com.manage.customer.entity.MemberBenefit;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.entity.RequestCountDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer getId(String id); // 상세 검색
    void updateMember(Customer member, String benefitsCsv); // 회원 정보 수정
    void deleteCustomer(String id); // 회원 정보 삭제
    Page<Customer> searchCustomers(String keyword, String searchType, int page, int size);
    
    List<MemberCountDTO> getDailyNewMembers();
    List<RequestCountDTO> getDailyRequests();
    
    // 회원 등급 관련
    List<Customer> getAdminCustomers();
    void savePermission(String memberId, String permName); // 관리자 권한 추가
    List<String> getRolesByCustomerId(String customerId); // 권한 정보 불러오기
    
    String login(String id, String passwd); // 로그인
    Customer loginAndGetCustomer(String id, String passwd); // 로그인 정보
    
    boolean hasPermission(String memberId, String permName); // 관리자 권한별 허용 탐지
    
    // 베네핏 리스트
    List<MemberBenefit> getBenefitsByMemberId(String memberId);
    List<Benefit> searchBenefits(String keyword);
    
    List<GenderAgeOrderDTO> getGenderAgeOrderStats(); // 구매자 성별 통계
    
    void registerCustomer(Customer customer); // 회원가입
    
    Optional<Customer> findByNameAndEmail(String name, String email);
    
    boolean existsById(String id);
    
    Optional<Customer> findByIdAndName(String id, String name);
    void updatePassword(String id, String newPassword);
}