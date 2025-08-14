package com.manage.order.service;

import org.springframework.stereotype.Service;

import com.manage.order.entity.Order;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository oderRepository) {
        this.orderRepository = oderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    public List<OrderStatsDTO> getOrderStats() {
        List<Object[]> results = orderRepository.getOrderStatsRaw();
        List<OrderStatsDTO> stats = new ArrayList<>();

        for (Object[] row : results) {
            String orderDate = (String) row[0];
            Long customerCount = ((Number) row[1]).longValue();
            Long totalSum = ((Number) row[2]).longValue();

            stats.add(new OrderStatsDTO(orderDate, customerCount, totalSum));
        }

        return stats;
    }
}
