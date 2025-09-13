package com.manage.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;
import java.util.*;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import com.manage.order.entity.Order;
import com.manage.order.entity.ReturnImage;
import com.manage.order.entity.ReturnRequest;
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
    public String orderList(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

    	Pageable pageable = PageRequest.of(page, size,
    	        Sort.by(Sort.Direction.DESC, "lastUpdated").and(Sort.by(Sort.Direction.DESC, "orderDate")));
        Page<Order> ordersPage;

        // 검색 조건 처리
        if ("customerName".equals(searchType) && keyword != null && !keyword.isBlank()) {
            ordersPage = orderService.searchOrdersByCustomerName(keyword, pageable);
        } else if ("orderStatus".equals(searchType) && keyword != null && !keyword.isBlank()) {
            ordersPage = orderService.searchOrdersByOrderStatus(keyword, pageable);
        } else {
            ordersPage = orderService.searchOrders(null, null, pageable); // 전체 조회
        }

        List<Order> orders = ordersPage.getContent();

        // 상품 ID → 상품명 맵 생성
        Map<Long, String> productNameMap = productRepository.findAll().stream()
                .collect(Collectors.toMap(p -> p.getProductId(), p -> p.getName()));

        // 주문별 상품명 목록 생성
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

        // NEW 여부 판단
        Map<Long, Boolean> isNewMap = new HashMap<>();
        for (Order order : orders) {
            isNewMap.put(order.getOrderId(), isNewOrder(order));
        }

        // 검색 파라미터 처리
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("searchType", searchType != null ? searchType : "");
        paramMap.put("keyword", keyword != null ? keyword : "");

        model.addAttribute("orders", orders);
        model.addAttribute("orderProductNames", orderProductNames);
        model.addAttribute("isNewMap", isNewMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("param", paramMap);

        return "manage/order/orderlist";
    }

    // ✅ NEW 여부 판단 메서드
    private boolean isNewOrder(Order order) {
        LocalDate today = LocalDate.now();
        return (order.getOrderDate() != null && today.equals(order.getOrderDate())) ||
               (order.getLastUpdated() != null && today.equals(order.getLastUpdated()));
    }

    @PostMapping
    public String saveOrder(@ModelAttribute Order order) {
        // productId, productQuantities, detailOption은 이미 문자열로 들어옴
        // 따로 join이나 List 변환 불필요
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
    public ResponseEntity<byte[]> getMainImage(@PathVariable Long id) {
    	Product product = productService.getProductById(id);

        if (product == null || product.getImageData() == null || product.getImageData().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String imageType = product.getImageType();
        MediaType mediaType;

        try {
            if (imageType == null || !imageType.startsWith("image/")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                if (imageType.equalsIgnoreCase("image/jpg")) {
                    imageType = "image/jpeg";
                }
                mediaType = MediaType.parseMediaType(imageType);
            }
        } catch (InvalidMimeTypeException e) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(product.getImageData());
    }
    
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
    
    @GetMapping("/return")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("requestDate").descending());
        Page<ReturnRequest> resultPage = orderService.search(keyword, pageable);

        model.addAttribute("returns", resultPage.getContent());
        model.addAttribute("page", resultPage);
        model.addAttribute("keyword", keyword);

        return "manage/order/returnlist";
    }
    
    // 반품 상세 조회
    @GetMapping("/return/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ReturnRequest request = orderService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반품 요청입니다."));

        List<ReturnImage> images = orderService.getImagesByReturnId(id);

        model.addAttribute("request", request);
        model.addAttribute("images", images); // 이미지 리스트도 같이 넘김

        return "manage/order/returndetail";
    }
    
    @PostMapping("/update")
    public String updateReturnStatus(
            @RequestParam("returnId") Long returnId,
            @RequestParam("status") String status,
            @RequestParam(value = "rejectReason", required = false) String rejectReason,
            RedirectAttributes redirectAttributes
    ) {
        try {
            orderService.updateReturnStatus(returnId, status, rejectReason);
            redirectAttributes.addFlashAttribute("message", "반품 정보가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "저장 중 오류가 발생했습니다.");
        }

        return "redirect:/orders/return/" + returnId;
    }

    @GetMapping("/return/image/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        ReturnImage image = orderService.getImageById(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 필요시 PNG로 변경
        return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
    }
}
