package com.manage.customer.service;

import com.manage.customer.entity.AdminPermission;
import com.manage.customer.entity.Customer;
import com.manage.customer.entity.GenderAgeOrderDTO;
import com.manage.customer.entity.MemberBenefit;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.entity.RequestCountDTO;
import com.manage.customer.repository.AdminPermissionRepository;
import com.manage.customer.repository.CustomerRepository;
import com.manage.customer.repository.MemberBenefitRepository;

import org.springframework.stereotype.Service;
import com.manage.benefit.entity.Benefit;
import com.manage.benefit.repository.BenefitRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final AdminPermissionRepository adminPermissionRepository;
	private final BenefitRepository benefitRepository;
	private final MemberBenefitRepository memberBenefitRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
    		AdminPermissionRepository adminPermissionRepository, BenefitRepository benefitRepository,
    		MemberBenefitRepository memberBenefitRepository) {
        this.customerRepository = customerRepository;
        this.adminPermissionRepository = adminPermissionRepository;
        this.benefitRepository = benefitRepository;
        this.memberBenefitRepository = memberBenefitRepository;
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
    
    @Transactional
    public void updateMember(Customer member, String benefitsCsv) {
        // 회원 정보 업데이트
        customerRepository.save(member);

        // CSV -> MemberBenefit 리스트
        List<MemberBenefit> parsed = Arrays.stream(Optional.ofNullable(benefitsCsv).orElse("").split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(s -> {
                String[] parts = s.split("\\|\\|", 2);
                MemberBenefit mb = new MemberBenefit();
                mb.setMemberId(member.getId());
                mb.setBenefitName(parts[0].trim());
                mb.setBenefitDescription(parts.length > 1 ? parts[1].trim() : "");
                return mb;
            })
            .collect(Collectors.toList());

        // 기존 혜택 삭제
        memberBenefitRepository.deleteByMemberId(member.getId());

        // 새로운 혜택 모두 저장
        memberBenefitRepository.saveAll(parsed);
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
    
    // 관리자 권한 및 직책 보기
    @Override
    public List<Customer> getAdminCustomers() {
        // 사원, 매니저, 운영자
        List<String> adminGrades = Arrays.asList("사원", "매니저", "운영자");
        return customerRepository.findByGradeIn(adminGrades);
    }
    
    // 관리자 권한 부여
    @Override
    public void savePermission(String memberId, String permName) {
        AdminPermission permission = new AdminPermission();
        permission.setMemberId(memberId);
        permission.setPermName(permName);
        adminPermissionRepository.save(permission); // insert 또는 update
    }
    
    @Override
    public List<String> getRolesByCustomerId(String customerId) {
        AdminPermission permission = adminPermissionRepository.findByMemberId(customerId);
        if (permission != null && permission.getPermName() != null && !permission.getPermName().isEmpty()) {
            // 콤마 기준으로 분리
            return Arrays.asList(permission.getPermName().split(","));
        }
        return Collections.emptyList();
    }
    
    @Transactional(readOnly = true)
    public String login(String id, String passwd) {
        Customer customer = customerRepository.findByIdAndPasswd(id, passwd);
        if (customer == null) {
            return "아이디 또는 비밀번호가 잘못되었습니다.";
        }

        // grade 체크
        String grade = customer.getGrade();
        if ("사원".equals(grade) || "매니저".equals(grade) || "운영자".equals(grade)) {
            return "로그인 성공";
        } else {
            return "관리자만 로그인 가능합니다.";
        }
    }
    
    // 로그인자 정보 불러오기
    @Override
    @Transactional(readOnly = true)
    public Customer loginAndGetCustomer(String id, String passwd) {
        // 아이디 + 비밀번호로 조회
        return customerRepository.findByIdAndPasswd(id, passwd);
    }
    
    // 관리자 권한
    @Override
    public boolean hasPermission(String memberId, String permName) {
        AdminPermission permission = adminPermissionRepository.findByMemberId(memberId);
        if (permission == null || permission.getPermName() == null) {
            return false;
        }

        String[] perms = permission.getPermName().split(",");
        for (String perm : perms) {
            if (perm.trim().equals(permName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Benefit> searchBenefits(String keyword) {
        return benefitRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    // 물건 판매 추세
    @Transactional
    public void removeBenefitFromCustomer(String customerId, String benefitName) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String benefitsStr = customer.getBenefits();
        if (benefitsStr == null || benefitsStr.isEmpty()) return;

        List<String> list = new ArrayList<>(Arrays.asList(benefitsStr.split(",")));
        if (list.remove(benefitName)) { // 해당 혜택 제거
            customer.setBenefits(String.join(",", list));
            customerRepository.save(customer);
        }
    }
    
    // 이용 남녀 성별
    @Override
    public List<GenderAgeOrderDTO> getGenderAgeOrderStats() {
        List<Object[]> results = customerRepository.getGenderAgeOrderStats();
        List<GenderAgeOrderDTO> list = new ArrayList<>();

        for (Object[] row : results) {
            String gender = row[0] != null ? row[0].toString() : "기타";
            int age = row[1] != null ? ((Number) row[1]).intValue() : 0;
            Long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;

            list.add(new GenderAgeOrderDTO(gender, age, count));
        }

        return list;
    }
    
    @Override
    public List<MemberBenefit> getBenefitsByMemberId(String memberId) {
        return memberBenefitRepository.findByMemberId(memberId);
    }
}