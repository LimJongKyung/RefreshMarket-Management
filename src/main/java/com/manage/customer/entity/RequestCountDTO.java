package com.manage.customer.entity;

public class RequestCountDTO {
	private String day;
    private Long count;

    public RequestCountDTO(String day, Long count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public Long getCount() {
        return count;
    }
}
