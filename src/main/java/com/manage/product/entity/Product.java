package com.manage.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
	@SequenceGenerator(name = "product_seq_gen", sequenceName = "PRODUCT_ID_SEQ", allocationSize = 1)
    private Long productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Integer stock;

    @Lob
    @Column(name = "IMAGE")
    private byte[] imageData;
    
    @Column(name = "IMAGE_TYPE")
    private String imageType;

    @Column(length = 100)  // 문자열 카테고리
    private String category;
    
    @Column(name = "MAIN_DISPLAY", length = 200)
    private String mainDisplay;
    
    @Column(length = 100)
    private String seller;  // 판매자

    @Column(length = 100)
    private String manufacturer;  // 제조업체

    @Column(name = "DETAIL_OPTION", length = 500)
    private String detailOption;  // 상세선택
    
    @Column(name = "DETAIL_OPTION_PRICE", length = 500)
    private String detailOptionPrice;

    public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDetailOption() {
		return detailOption;
	}

	public void setDetailOption(String detailOption) {
		this.detailOption = detailOption;
	}

	@Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public String getMainDisplay() {
		return mainDisplay;
	}

	public void setMainDisplay(String mainDisplay) {
		this.mainDisplay = mainDisplay;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getDetailOptionPrice() {
		return detailOptionPrice;
	}

	public void setDetailOptionPrice(String detailOptionPrice) {
		this.detailOptionPrice = detailOptionPrice;
	}
}