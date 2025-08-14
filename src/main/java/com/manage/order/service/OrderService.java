package com.manage.order.service;

import java.util.List;

import com.manage.order.entity.Order;
import com.manage.order.entity.OrderStatsDTO;

public interface OrderService {
	List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order saveOrder(Order order);
    void deleteOrder(Long id);
    
    List<OrderStatsDTO> getOrderStats();
}
