package com.manage.order.service;

import org.springframework.stereotype.Service;

import com.manage.order.entity.DailyProductSaleDTO;
import com.manage.order.entity.Order;
import com.manage.order.entity.OrderStatsDTO;
import com.manage.order.entity.ReturnImage;
import com.manage.order.entity.ReturnRequest;
import com.manage.order.repository.OrderRepository;
import com.manage.order.repository.ReturnImageRepository;
import com.manage.order.repository.ReturnRequestRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;
	private final ReturnRequestRepository returnRepository;
	private final ReturnImageRepository returnImageRepository;

    public OrderServiceImpl(OrderRepository oderRepository, ReturnRequestRepository returnRepository
    		,ReturnImageRepository returnImageRepository) {
        this.orderRepository = oderRepository;
        this.returnRepository = returnRepository;
        this.returnImageRepository = returnImageRepository;
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
    
    @Override
    public Page<Order> searchOrders(String customerName, String status, Pageable pageable) {
        if ((customerName == null || customerName.isEmpty()) && (status == null || status.isEmpty())) {
            return orderRepository.findAll(pageable);
        } else if (customerName != null && !customerName.isEmpty() && (status == null || status.isEmpty())) {
            return orderRepository.findByCustomerNameContaining(customerName, pageable);
        } else if ((customerName == null || customerName.isEmpty()) && status != null && !status.isEmpty()) {
            return orderRepository.findByOrderStatus(status, pageable);
        } else {
            return orderRepository.findByCustomerNameContainingAndOrderStatus(customerName, status, pageable);
        }
    }
    
    @Override
    public Page<Order> searchOrdersByCustomerName(String customerName, Pageable pageable) {
        return orderRepository.findByCustomerNameContaining(customerName, pageable);
    }

    @Override
    public Page<Order> searchOrdersByOrderStatus(String orderStatus, Pageable pageable) {
        return orderRepository.findByOrderStatus(orderStatus, pageable);
    }
    
    @Override
    public List<DailyProductSaleDTO> getDailyProductSales() {
        List<Object[]> results = orderRepository.getDailyProductSales();
        List<DailyProductSaleDTO> list = new ArrayList<>();

        for (Object[] row : results) {
            String orderDate = row[0] != null ? row[0].toString() : "";
            String productName = row[1] != null ? row[1].toString() : "";
            Long totalQuantity = row[2] != null ? ((Number) row[2]).longValue() : 0L;

            list.add(new DailyProductSaleDTO(orderDate, productName, totalQuantity));
        }

        return list;
    }

    @Override
    public boolean isNewOrder(Order order) {
        LocalDate today = LocalDate.now();
        return today.equals(order.getOrderDate()) || today.equals(order.getLastUpdated());
    }
    
    @Override
    public Page<ReturnRequest> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return returnRepository.findAll(pageable);
        }
        return returnRepository.searchByKeyword(keyword.trim(), pageable);
    }
    
    @Override
    public Optional<ReturnRequest> findById(Long id) {
        return returnRepository.findById(id);
    }
    
    @Override
    public void updateReturnStatus(Long returnId, String status, String rejectReason) {
        ReturnRequest request = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("해당 반품 요청을 찾을 수 없습니다."));

        request.setStatus(status);
        request.setRejectReason(rejectReason);
        returnRepository.save(request);
    }
    
    @Override
    public List<ReturnImage> getImagesByReturnId(Long returnId) {
        return returnImageRepository.findByReturnRequest_ReturnId(returnId);
    }

    @Override
    public ReturnImage getImageById(Long imageId) {
        return returnImageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
