package com.manage.customer.service;

import com.manage.customer.entity.Customer;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.entity.RequestCountDTO;
import com.manage.customer.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    // 리스트
    @Override
    public Page<Customer> searchCustomers(String keyword, String searchType, int page, int size) {
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "joinDate"));

        if (keyword == null || keyword.isEmpty()) {
            return customerRepository.findAll(pageable);
        }

        if ("name".equalsIgnoreCase(searchType)) {
            return customerRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if("id".equalsIgnoreCase(searchType)) {
            return customerRepository.findByIdContainingIgnoreCase(keyword, pageable);
        } else {
        	return customerRepository.findByAddressContainingIgnoreCase(keyword, pageable);
        }
    }
    
    // 디테일 및 수정에 필요한 정보 확인
    @Override
    public Customer getId(String id) {
    	return customerRepository.findById(id)
                .orElse(null);
    }
    
    // 수정
    @Override
    public void updateMember(Customer customer) {
        // DB에 저장할 때 기존 회원 존재 여부 및 비밀번호 체크는 컨트롤러나 별도로 처리 가능
        customerRepository.save(customer);
    }
       
    // 삭제
    @Override
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
    
    @Override
    public List<MemberCountDTO> getDailyNewMembers() {
        List<Object[]> rows = customerRepository.countNewMembersPerDayRaw();
        return rows.stream()
                .map(r -> new MemberCountDTO((String) r[0], ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RequestCountDTO> getDailyRequests() {
        List<Object[]> rows = customerRepository.countRequestsPerDayRaw();
        return rows.stream()
                .map(r -> new RequestCountDTO((String) r[0], ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }
}