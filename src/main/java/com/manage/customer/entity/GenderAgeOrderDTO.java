package com.manage.customer.entity;

public class GenderAgeOrderDTO {
    private String gender;
    private int age;
    private Long orderCount;

    public GenderAgeOrderDTO(String gender, int age, Long orderCount) {
        this.gender = gender;
        this.age = age;
        this.orderCount = orderCount;
    }

    // getter / setter
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public Long getOrderCount() { return orderCount; }
    public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }
}