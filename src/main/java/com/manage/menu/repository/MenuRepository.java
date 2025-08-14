package com.manage.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manage.menu.entity.Menu;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByPosition(String position);
}