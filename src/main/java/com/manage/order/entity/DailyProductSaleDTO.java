package com.manage.order.entity;

public class DailyProductSaleDTO {
    private String orderDate;
    private String productName;
    private Long totalQuantity;

    public DailyProductSaleDTO(String orderDate, String productName, Long totalQuantity) {
        this.orderDate = orderDate;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }

    // getter / setter 필요
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Long totalQuantity) { this.totalQuantity = totalQuantity; }
}
