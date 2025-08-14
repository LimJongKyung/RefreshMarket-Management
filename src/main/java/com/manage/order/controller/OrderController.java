package com.manage.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import com.manage.order.entity.Order;
import com.manage.order.service.OrderService;
import com.manage.product.entity.Product;
import com.manage.product.repository.ProductRepository;
import com.manage.product.service.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final ProductRepository productRepository;
	private final ProductService productService;

    public OrderController(OrderService orderService, ProductRepository productRepository, ProductService productService) {
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.productService = productService;
    }
    
    @GetMapping
    public String orderList(Model model) {
        List<Order> orders = orderService.getAllOrders();

        // productId(Long) -> productName(String) 맵 생성
        Map<Long, String> productNameMap = productRepository.findAll().stream()
            .collect(Collectors.toMap(Product::getProductId, Product::getName));

        // 주문마다 상품 이름 목록 생성 (키: order.getOrderId() Long)
        Map<Long, String> orderProductNames = new HashMap<>();
        for (Order order : orders) {
            if (order.getProductId() == null || order.getProductId().isBlank()) {
                orderProductNames.put(order.getOrderId(), "상품 없음");
                continue;
            }
            
            String[] idStrings = order.getProductId().split(",");
            List<String> names = Arrays.stream(idStrings)
                .map(String::trim)
                .map(idStr -> {
                    try {
                        Long id = Long.parseLong(idStr);
                        return productNameMap.getOrDefault(id, "상품 없음");
                    } catch (NumberFormatException e) {
                        return "잘못된 ID";
                    }
                }).toList();

            orderProductNames.put(order.getOrderId(), String.join(", ", names));
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderProductNames", orderProductNames);
        return "manage/order/orderlist";
    }

    @PostMapping
    public String saveOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/orders";
    }
    
    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("orderInfo", order);

        List<Product> productList = new ArrayList<>();
        List<Integer> quantityList = new ArrayList<>();
        List<String> optionList = new ArrayList<>();

        if (order.getProductId() != null && !order.getProductId().isBlank()) {
            String[] idStrings = order.getProductId().split(",");
            String[] quantityStrings = order.getProductQuantities() != null
                    ? order.getProductQuantities().split(",")
                    : new String[0];
            String[] optionStrings = order.getDetailOption() != null
                    ? order.getDetailOption().split(",")
                    : new String[0];

            for (int i = 0; i < idStrings.length; i++) {
                try {
                    Long productId = Long.parseLong(idStrings[i].trim());
                    productRepository.findById(productId).ifPresent(productList::add);

                    if (i < quantityStrings.length) {
                        quantityList.add(Integer.parseInt(quantityStrings[i].trim()));
                    } else {
                        quantityList.add(1); // fallback
                    }

                    if (i < optionStrings.length) {
                        optionList.add(optionStrings[i].trim());
                    } else {
                        optionList.add("없음"); // fallback
                    }

                } catch (NumberFormatException e) {
                    System.err.println("잘못된 productId 또는 quantity 형식: " + idStrings[i]);
                }
            }
        }

        model.addAttribute("productList", productList);
        model.addAttribute("quantityList", quantityList);
        model.addAttribute("optionList", optionList); // ✅ 옵션 리스트 추가

        return "manage/order/orderedit";
    }

    
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String mimeType = product.getImageType();

        // MIME 타입이 없으면 기본값 지정 (예: JPEG)
        if (mimeType == null || mimeType.isBlank()) {
            mimeType = "image/jpeg"; // 또는 "application/octet-stream"
        }

        headers.setContentType(MediaType.parseMediaType(mimeType));
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
    
}
