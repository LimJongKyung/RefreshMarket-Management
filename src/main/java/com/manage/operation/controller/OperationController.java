package com.manage.operation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import com.manage.customer.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.manage.customer.entity.Customer;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OperationController {

    private final CustomerService customerService;
    
    public OperationController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // 권한수정 관리자 페이지
    @GetMapping("/operationM")
    public String operationM(Model model) {
        List<Customer> adminCustomers = customerService.getAdminCustomers();
        model.addAttribute("adminCustomers", adminCustomers);
        return "manage/policy/operationM";
    }
    
    @GetMapping("/operationM/detail/{customerId}")
    public String operationMDetail(@PathVariable String customerId, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");

        // "회원 관리" 권한 체크
        if (!customerService.hasPermission(loginId, "관리자 계정 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/operationM';</script>");
            response.getWriter().flush();
            return null; // 상세 페이지 접근 막기
        }

        Customer customer = customerService.getId(customerId); 
        List<String> customerRoles = customerService.getRolesByCustomerId(customerId); // 권한 리스트 가져오기

        model.addAttribute("customer", customer);
        model.addAttribute("customerRoles", customerRoles); // Thymeleaf에서 체크박스 자동 체크용

        return "manage/policy/operationMdetail";
    }

    // 권한 저장
    @PostMapping("/operationM/assignRoles")
    public String assignRoles(@RequestParam("customerId") String customerId,
                              @RequestParam(value = "roles", required = false) List<String> roles) {

        String rolesStr = (roles != null && !roles.isEmpty()) ? String.join(",", roles) : "";

        customerService.savePermission(customerId, rolesStr);

        return "redirect:/operationM/detail/" + customerId;
    }
    
    @GetMapping("/operationC")
    public String operationC(HttpSession session, HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");

        // "회원 관리" 권한 체크
        if (!customerService.hasPermission(loginId, "할인 정책 설정")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/home';</script>");
            response.getWriter().flush();
            return null; // 접근 막기
        }

        return "manage/policy/operationC";
    }
}