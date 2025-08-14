package com.manage.customer.service;

import com.manage.customer.entity.Customer;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.entity.RequestCountDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer getId(String id); // 상세 검색
    void updateMember(Customer customer); // 회원 정보 수정
    void deleteCustomer(String id); // 회원 정보 삭제
    Page<Customer> searchCustomers(String keyword, String searchType, int page, int size);
    
    List<MemberCountDTO> getDailyNewMembers();
    List<RequestCountDTO> getDailyRequests();
}