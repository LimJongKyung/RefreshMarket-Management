package com.manage.requests.controller;

import com.manage.requests.entity.Request;
import com.manage.requests.service.RequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public String listRequests(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> requestPage = requestService.getRequests(keyword, pageable);

        model.addAttribute("requestPage", requestPage);
        model.addAttribute("keyword", keyword);
        return "manage/request/request";
    }
    
    @GetMapping("/{id}")
    public String getRequestDetail(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id);
        model.addAttribute("request", request);
        return "manage/request/detailR"; // templates/requests/detail.html
    }
}
