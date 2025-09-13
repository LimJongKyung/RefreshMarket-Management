package com.manage.menu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.manage.customer.service.CustomerService;
import com.manage.menu.entity.Menu;
import com.manage.menu.service.MenuService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;
    private final CustomerService customerService;

    public MenuController(MenuService menuService, CustomerService customerService) {
        this.menuService = menuService;
        this.customerService = customerService;
    }

    // 메뉴 관리 페이지 (목록)
    @GetMapping
    public String menuManagePage(Model model) {
        List<Menu> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<Menu> headerMenus = menuService.getMenusByPosition("header");
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        return "manage/menu/menu";  // resources/templates/menu/manage.html
    }

    // 메뉴 추가 (폼 submit)
    @PostMapping("/add")
    public String addMenu(@RequestParam String name,
                          @RequestParam(required = false, defaultValue = "0") Integer orderNum,
                          @RequestParam String position,
                          HttpSession session,
                          HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "메뉴 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/menus';</script>");
            response.getWriter().flush();
            return null;
        }

        Menu menu = new Menu();
        menu.setName(name);
        menu.setPosition(position);
        menuService.createMenu(menu);
        return "redirect:/menus";
    }

    // 메뉴 삭제 (폼 submit)
    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Long id,
                             HttpSession session,
                             HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "메뉴 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/menus';</script>");
            response.getWriter().flush();
            return null;
        }

        menuService.deleteMenu(id);
        return "redirect:/menus";
    }

    // 메뉴 이름 수정 (폼 submit)
    @PostMapping("/update")
    public String updateMenuName(@RequestParam Long id,
                                 @RequestParam String name,
                                 HttpSession session,
                                 HttpServletResponse response) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        if (!customerService.hasPermission(loginId, "메뉴 관리")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('권한이 없습니다!'); location.href='/menus';</script>");
            response.getWriter().flush();
            return null;
        }

        menuService.updateMenuName(id, name);
        return "redirect:/menus";
    }
}
