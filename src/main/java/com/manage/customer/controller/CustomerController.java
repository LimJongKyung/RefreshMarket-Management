package com.manage.customer.controller;

import com.manage.customer.entity.Customer;
import com.manage.customer.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerService customerService;

	@Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
	
	// 리스트
	@GetMapping
	public String getCustomerList(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(required = false) String keyword,
	        @RequestParam(defaultValue = "id") String searchType,
	        Model model) {

	    int pageSize = 20;
	    Page<Customer> boardPage = customerService.searchCustomers(keyword, searchType, page, pageSize);
	    boolean searchTried = keyword != null && !keyword.trim().isEmpty();
	    
	    model.addAttribute("boardPage", boardPage);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("searchType", searchType);
	    model.addAttribute("searchTried", searchTried); // 검색 시도 여부 플래그

	    return "manage/member/customer";
	}
    
    // 디테일
    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        Customer customer = customerService.getId(id);
        if (customer == null) {
            return "error/404"; // 없는 경우 처리
        }
        model.addAttribute("member", customer);
        return "manage/member/detailC"; // 상세보기 HTML
    }
    
    // 수정 폼 불러오기
    @GetMapping("/updateForm")
    public String updateForm(@RequestParam("id") String id, Model model) {
        Customer member = customerService.getId(id);
        if (member == null) {
            return "redirect:/error"; // 회원 없으면 에러페이지로
        }
        model.addAttribute("member", member);
        return "manage/member/modifyC"; // 위에 만든 Thymeleaf 파일 이름
    }

    // 수정 처리
    @PostMapping("/update")
    public String update(Customer member, Model model) {
    	Customer existingMember = customerService.getId(member.getId());
        if (existingMember == null) {
            model.addAttribute("msg", "회원이 존재하지 않습니다.");
            return "error";
        }
        // 보통 비밀번호 확인 로직이 필요하지만 생략 가능

        // 기존 가입일 유지
        member.setJoinDate(existingMember.getJoinDate());

        customerService.updateMember(member);

        return "redirect:/customer/" + member.getId();
    }
    
    // 삭제 처리
    @PostMapping("/delete")
    public String deleteCustomer(@RequestParam("id") String id) {
        customerService.deleteCustomer(id);
        return "redirect:/customer"; // 삭제 후 목록 페이지로 이동
    }
    
}