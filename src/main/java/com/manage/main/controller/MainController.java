package com.manage.main.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manage.customer.entity.Customer;
import com.manage.customer.entity.GenderAgeOrderDTO;
import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.service.CustomerService;
import com.manage.mail.service.MailService;
import com.manage.order.entity.DailyProductSaleDTO;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	private final OrderService orderService;
	private final CustomerService customerService;
	private final MailService mailService;

    public MainController(OrderService orderService, CustomerService customerService
    						,MailService mailService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.mailService = mailService;
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

    @GetMapping("/signup")
    public String signup() {
        return "manage/login/signup";
    }
    
    @GetMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        boolean exists = customerService.existsById(id);
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping("/signup")
    @ResponseBody
    public String registerCustomer(@ModelAttribute Customer customer) {
        try {
            customer.setJoinDate(LocalDateTime.now());
            customer.setGrade("일반회원");
            customer.setBenefits("");

            customerService.registerCustomer(customer);

            return "<script>alert('회원가입이 완료되었습니다.'); location.href='/';</script>";
        } catch (IllegalArgumentException e) {
            return "<script>alert('" + e.getMessage() + "'); location.href='/signup';</script>";
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
    
    @GetMapping("/findid")
    public String findId () {
    	return "manage/login/findid";
    }
    
    @PostMapping("/send-id")
    public String sendId(@RequestParam String name,
                         @RequestParam String email,
                         Model model) {
        Optional<Customer> customerOpt = customerService.findByNameAndEmail(name, email);

        if (customerOpt.isPresent()) {
            String userId = customerOpt.get().getId();
            mailService.sendIdEmail(email, userId);
            model.addAttribute("msg", "아이디가 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("msg", "일치하는 회원 정보가 없습니다.");
        }

        return "manage/login/findid";
    }
    
    @GetMapping("/findpasswd")
    public String findpassword () {
    	return "manage/login/findpasswd";
    }
    
    @PostMapping("/send-password")
    public String sendPassword(@RequestParam String id,
                               @RequestParam String name,
                               Model model) {
        Optional<Customer> customerOpt = customerService.findByIdAndName(id, name);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            String tempPassword = UUID.randomUUID().toString().substring(0, 10); // 임시 비밀번호 생성
            customerService.updatePassword(id, tempPassword);
            mailService.sendTempPasswordEmail(customer.getEmail(), tempPassword);
            model.addAttribute("msg", "임시 비밀번호가 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("msg", "일치하는 회원 정보가 없습니다.");
        }

        return "manage/login/findpasswd";
    }
}
