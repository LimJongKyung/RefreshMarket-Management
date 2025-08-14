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

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 카드, 계좌이체

    @Enumerated(EnumType.STRING)
    private BankStatus bankStatus; // 입금, 입금하지 않음

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문확인, 입금확인, 배송중, 배송완료, 주문취소

    private LocalDate orderDate;
    private String phoneNumber;
    private String email;
    private String deliveryRequest;
    private LocalDate deliveryDate;
    private LocalDate deliveryCompleteDate;

    private boolean isCanceled;
    private LocalDate lastUpdated;

    // Enum 내부 정의
    public enum PaymentMethod {
        카드, 계좌이체
    }

    public enum BankStatus {
        입금, 입금하지_않음
    }

    public enum OrderStatus {
        주문확인, 입금확인, 배송중, 배송완료, 주문취소
    }

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(String productQuantities) {
        this.productQuantities = productQuantities;
    }
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getDetailOption() {
	    return detailOption;
	}

	public void setDetailOption(String detailOption) {
	    this.detailOption = detailOption;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BankStatus getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(BankStatus bankStatus) {
		this.bankStatus = bankStatus;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(String deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public LocalDate getDeliveryCompleteDate() {
		return deliveryCompleteDate;
	}

	public void setDeliveryCompleteDate(LocalDate deliveryCompleteDate) {
		this.deliveryCompleteDate = deliveryCompleteDate;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
