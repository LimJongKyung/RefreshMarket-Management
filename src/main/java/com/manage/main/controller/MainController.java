package com.manage.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manage.customer.entity.Customer;
import com.manage.customer.entity.GenderAgeOrderDTO;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.service.CustomerService;
import com.manage.order.entity.DailyProductSaleDTO;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	private final OrderService orderService;
	private final CustomerService customerService;

    public MainController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }
    
    @GetMapping("/")
    public String login() {
    	return "manage/login/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String passwd,
                        HttpSession session,
                        Model model) {

        Customer customer = customerService.loginAndGetCustomer(id, passwd);

        if (customer != null) {
            String grade = customer.getGrade();
            if ("사원".equals(grade) || "매니저".equals(grade) || "운영자".equals(grade)) {
                session.setAttribute("loginId", customer.getId());
                session.setAttribute("loginName", customer.getName());
                session.setAttribute("loginGrade", customer.getGrade());

                int sessionTimeoutSeconds = 60 * 60; // 1시간
                session.setMaxInactiveInterval(sessionTimeoutSeconds);

                // 클라이언트에서 쓸 세션 만료 타임 계산
                long expiryTimeMillis = System.currentTimeMillis() + sessionTimeoutSeconds * 1000L;
                session.setAttribute("sessionExpiryTime", expiryTimeMillis);

                return "redirect:/home";
            } else {
                model.addAttribute("msg", "관리자만 로그인 가능합니다.");
                return "manage/login/login";
            }
        } else {
            model.addAttribute("msg", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "manage/login/login";
        }
    }

    @GetMapping("/home")
    public String main(Model model) {
    	List<OrderStatsDTO> orderStats = orderService.getOrderStats();
        List<MemberCountDTO> customerStats = customerService.getDailyNewMembers();
        List<DailyProductSaleDTO> dailyProductSales = orderService.getDailyProductSales();
        List<GenderAgeOrderDTO> genderAgeStats = customerService.getGenderAgeOrderStats();
        
        model.addAttribute("dailyProductSales", dailyProductSales);
        model.addAttribute("orderStats", orderStats);
        model.addAttribute("customerStats", customerStats);
        model.addAttribute("requestStats", customerService.getDailyRequests());
        model.addAttribute("genderAgeStats", genderAgeStats);
        return "manage/main";
    }
}
