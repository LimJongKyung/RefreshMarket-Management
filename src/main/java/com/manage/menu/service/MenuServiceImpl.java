package com.manage.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manage.menu.entity.Menu;
import com.manage.menu.repository.MenuRepository;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getMenusByPosition(String position) {
        return menuRepository.findByPosition(position);
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public Menu updateMenuName(Long id, String newName) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid menu id:" + id));
        menu.setName(newName);
        return menuRepository.save(menu);
    }
    
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
}
