package com.manage.customer.controller;

import com.manage.benefit.entity.Benefit;
import com.manage.customer.entity.Customer;
import com.manage.customer.entity.MemberBenefit;
import com.manage.customer.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("searchTried", searchTried);

        return "manage/member/customer";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        Customer customer = customerService.getId(id);
        if (customer == null) {
            return "error/404";
        }

        // member_benefit 테이블 기반 혜택 가져오기
        List<MemberBenefit> memberBenefits = customerService.getBenefitsByMemberId(customer.getId());

        model.addAttribute("member", customer);
        model.addAttribute("memberBenefits", memberBenefits);

        return "manage/member/detailC";
    }

    // 업데이트 폼
    @GetMapping("/updateForm")
    public String updateForm(@RequestParam("id") String id, HttpSession session, Model model, HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");

        if (!customerService.hasPermission(loginId, "회원 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/customer';</script>");
            response.getWriter().flush();
            return null;
        }

        Customer member = customerService.getId(id);
        if (member == null) return "redirect:/customer";

        List<MemberBenefit> memberBenefits = customerService.getBenefitsByMemberId(member.getId());

        // ✅ name||description 형식으로 변환
        String tableBenefits = memberBenefits.stream()
                .map(mb -> mb.getBenefitName() + "||" + (mb.getBenefitDescription() == null ? "" : mb.getBenefitDescription()))
                .collect(Collectors.joining(","));

        model.addAttribute("member", member);
        model.addAttribute("memberBenefits", memberBenefits);
        model.addAttribute("combinedBenefits", tableBenefits); // 프론트 hidden input에 내려감

        return "manage/member/modifyC";
    }
    
    @PostMapping("/update")
    @ResponseBody
    public String update(Customer member, @RequestParam(required = false) String benefits, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "회원 관리")) {
            return "<script>alert('권한이 없습니다.');location.href='/customer';</script>";
        }

        Customer existingMember = customerService.getId(member.getId());
        if (existingMember == null) {
            return "<script>alert('회원이 존재하지 않습니다.');location.href='/customer';</script>";
        }

        member.setJoinDate(existingMember.getJoinDate());

        // ✅ Member + MemberBenefit 모두 업데이트
        customerService.updateMember(member, benefits);

        return "<script>alert('회원 정보가 수정되었습니다.');location.href='/customer/" + member.getId() + "';</script>";
    }

    // 삭제 처리
    @PostMapping("/delete")
    @ResponseBody
    public String deleteCustomer(@RequestParam("id") String id, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "회원 관리")) {
            return "<script>alert('권한이 없습니다.');location.href='/customer';</script>";
        }

        customerService.deleteCustomer(id);
        return "<script>alert('회원이 삭제되었습니다.');location.href='/customer';</script>";
    }
    
    @GetMapping("/search")
    @ResponseBody
    public List<Benefit> searchBenefit(@RequestParam("keyword") String keyword) {
        return customerService.searchBenefits(keyword);
    }

}
