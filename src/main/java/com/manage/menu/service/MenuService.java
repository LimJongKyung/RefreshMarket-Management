package com.manage.menu.service;

import com.manage.menu.entity.Menu;
import java.util.List;

public interface MenuService {
    List<Menu> getMenusByPosition(String position);
    Menu createMenu(Menu menu);
    void deleteMenu(Long id);
    Menu updateMenuName(Long id, String newName);
    List<Menu> getAllMenus();
}