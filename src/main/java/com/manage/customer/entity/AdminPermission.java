package com.manage.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ADMIN_PERMISSION")
@Getter
@Setter
public class AdminPermission {

    @Id
    @Column(name = "MEMBER_ID", length = 50, nullable = false)
    private String memberId;

    @Column(name = "PERM_NAME", length = 50, nullable = false)
    private String permName;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPermName() {
		return permName;
	}

	public void setPermName(String permName) {
		this.permName = permName;
	}  
}