package com.manage.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.manage.customer.entity.MemberCountDTO;
import com.manage.customer.service.CustomerService;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.service.OrderService;

@Controller
public class MainController {
	
	private final OrderService orderService;
	private final CustomerService customerService;

    public MainController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String main(Model model) {
    	List<OrderStatsDTO> orderStats = orderService.getOrderStats();
        List<MemberCountDTO> customerStats = customerService.getDailyNewMembers();

        model.addAttribute("orderStats", orderStats);
        model.addAttribute("customerStats", customerStats);
        model.addAttribute("requestStats", customerService.getDailyRequests());
        return "manage/main";
    }
}
