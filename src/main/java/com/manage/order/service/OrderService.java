package com.manage.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.manage.order.entity.DailyProductSaleDTO;
import com.manage.order.entity.Order;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.entity.ReturnImage;
import com.manage.order.entity.ReturnRequest;

public interface OrderService {
	// List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order saveOrder(Order order);
    void deleteOrder(Long id);
    
    List<OrderStatsDTO> getOrderStats();
    
    Page<Order> searchOrders(String customerName, String orderStatus, Pageable pageable);
    
    // 이름만 검색
    Page<Order> searchOrdersByCustomerName(String customerName, Pageable pageable);

    // 상태만 검색
    Page<Order> searchOrdersByOrderStatus(String orderStatus, Pageable pageable);
    
    List<DailyProductSaleDTO> getDailyProductSales();
    
    public boolean isNewOrder(Order order);
    
    Page<ReturnRequest> search(String keyword, Pageable pageable);
    
    Optional<ReturnRequest> findById(Long id);
    
    void updateReturnStatus(Long returnId, String status, String rejectReason);
    
    List<ReturnImage> getImagesByReturnId(Long returnId);
    ReturnImage getImageById(Long imageId);
}
