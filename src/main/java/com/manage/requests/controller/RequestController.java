package com.manage.requests.controller;

import com.manage.requests.entity.Request;
import com.manage.requests.service.RequestService;

import java.util.Collections;
import java.util.Map;

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

    // 리스트 페이지
    @GetMapping
    public String listRequests(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> requestPage = requestService.getRequests(keyword, pageable);

        model.addAttribute("requestPage", requestPage);
        model.addAttribute("keyword", keyword);
        return "manage/request/request"; // 리스트 템플릿
    }

 // 상세 페이지: 답변 읽기
    @GetMapping("/{id}")
    public String detailRequest(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id);
        model.addAttribute("request", request);
        return "manage/request/detailR";
    }

    // 답변 저장/수정
    @PostMapping("/{id}/answer")
    @ResponseBody
    public Map<String, String> saveAnswer(@PathVariable Long id, @RequestParam String answer) {
        try {
            requestService.saveAnswer(id, answer);
            return Collections.singletonMap("status", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("status", "FAIL");
        }
    }

    // 답변 삭제
    @PostMapping("/{id}/answer/delete")
    @ResponseBody
    public Map<String, String> deleteAnswer(@PathVariable Long id) {
        try {
            requestService.deleteAnswer(id);
            return Collections.singletonMap("status", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("status", "FAIL");
        }
    }
}