package com.manage.customer.entity;

public class MemberCountDTO {
    private String day;
    private Long count;

    public MemberCountDTO() {}

    public MemberCountDTO(String day, Long count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}