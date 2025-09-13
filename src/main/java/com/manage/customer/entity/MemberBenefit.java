package com.manage.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member_benefit")
@Getter
@Setter
public class MemberBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // 기본 키 컬럼
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "benefit_name", nullable = false)
    private String benefitName;

    @Column(name = "benefit_description", length = 4000)
    private String benefitDescription;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getBenefitDescription() {
		return benefitDescription;
	}

	public void setBenefitDescription(String benefitDescription) {
		this.benefitDescription = benefitDescription;
	}    
}