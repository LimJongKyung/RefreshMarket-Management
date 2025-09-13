package com.manage.benefit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BENEFIT")
@Getter
@Setter
public class Benefit {

    @Id
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;  // PK로 사용

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}