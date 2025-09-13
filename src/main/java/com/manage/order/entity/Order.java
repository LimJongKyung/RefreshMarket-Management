package com.manage.order.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_info")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String customerId;
    private String customerName;

    private String productId;
    private String productQuantities;
    private String detailOption;

    private int quantity;
    private int totalPrice;

    private String shippingAddress;

    private String paymentMethod; // 카드, 계좌이체
    private String bankStatus;    // 입금, 입금하지 않음
    private String orderStatus;   // 주문확인, 입금확인, 배송중, 배송완료, 주문취소

    private LocalDate orderDate;
    private String phoneNumber;
    private String email;
    private String deliveryRequest;
    private String trackingNumber;
    private LocalDate deliveryCompleteDate;

    private String isCanceled;
    private LocalDate lastUpdated;

    // getter / setter
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductQuantities() { return productQuantities; }
    public void setProductQuantities(String productQuantities) { this.productQuantities = productQuantities; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getDetailOption() { return detailOption; }
    public void setDetailOption(String detailOption) { this.detailOption = detailOption; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getBankStatus() { return bankStatus; }
    public void setBankStatus(String bankStatus) { this.bankStatus = bankStatus; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDeliveryRequest() { return deliveryRequest; }
    public void setDeliveryRequest(String deliveryRequest) { this.deliveryRequest = deliveryRequest; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public LocalDate getDeliveryCompleteDate() { return deliveryCompleteDate; }
    public void setDeliveryCompleteDate(LocalDate deliveryCompleteDate) { this.deliveryCompleteDate = deliveryCompleteDate; }
    public String isCanceled() { return isCanceled; }
    public void setCanceled(String isCanceled) { this.isCanceled = isCanceled; }
    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}
