package com.manage.product.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_DETAIL_IMAGES")
public class ProductDetailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_detail_image_seq_gen")
    @SequenceGenerator(
        name = "product_detail_image_seq_gen",
        sequenceName = "PRODUCT_DETAIL_IMAGE_SEQ",
        allocationSize = 1
    )
    private Long id;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId; // 어떤 상품의 상세 이미지인지

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "IMAGE_TYPE", length = 50)
    private String imageType; // "DETAIL" 등 구분 가능

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
}