package com.manage.order.entity;

public class OrderStatsDTO {
	private String orderDate;
    private Long customerCount;
    private Long totalSum;
    
    public OrderStatsDTO(String orderDate, Long customerCount, Long totalSum) {
        this.orderDate = orderDate;
        this.customerCount = customerCount;
        this.totalSum = totalSum;
    }
    
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public Long getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(Long customerCount) {
		this.customerCount = customerCount;
	}
	public Long getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(Long totalSum) {
		this.totalSum = totalSum;
	}
    
    
}
