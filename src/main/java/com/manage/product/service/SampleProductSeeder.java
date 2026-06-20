package com.manage.product.service;

import com.manage.product.entity.Product;
import com.manage.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public SampleProductSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> samples = List.of(
                sample(
                        "제주 햇감귤 3kg",
                        "상큼한 산지 직송 감귤 샘플 상품입니다. 이미지는 상품 수정 화면에서 업로드해 주세요.",
                        "15900",
                        120,
                        "과일",
                        "sale",
                        "제주 감귤 협동조합",
                        "기본 3kg, 선물 포장",
                        "0,3000"
                ),
                sample(
                        "강원 고랭지 감자 5kg",
                        "포슬포슬한 식감의 고랭지 감자 샘플 상품입니다. 대표 이미지를 직접 등록할 수 있습니다.",
                        "12900",
                        85,
                        "채소",
                        "group",
                        "강원 로컬푸드",
                        "기본 5kg, 대용량 10kg",
                        "0,9000"
                ),
                sample(
                        "국산 잡곡 10종 세트",
                        "현미, 보리, 수수 등 자주 쓰는 잡곡으로 구성한 샘플 상품입니다.",
                        "21900",
                        64,
                        "잡곡",
                        "promotion",
                        "우리곡물영농조합",
                        "소포장, 가족팩",
                        "0,6000"
                ),
                sample(
                        "프리미엄 토마토 2kg",
                        "샐러드와 주스에 모두 쓰기 좋은 토마토 샘플 상품입니다.",
                        "17900",
                        98,
                        "채소",
                        "sale",
                        "스마트팜 토마토팜",
                        "일반, 완숙",
                        "0,1500"
                )
        );

        samples.stream()
                .filter(product -> !productRepository.existsByName(product.getName()))
                .forEach(productRepository::save);
    }

    private Product sample(
            String name,
            String description,
            String price,
            Integer stock,
            String category,
            String mainDisplay,
            String manufacturer,
            String detailOption,
            String detailOptionPrice
    ) {
        Product product = new Product();
        LocalDateTime now = LocalDateTime.now();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setCategory(category);
        product.setMainDisplay(mainDisplay);
        product.setSeller("RefreshMarket 샘플");
        product.setManufacturer(manufacturer);
        product.setDetailOption(detailOption);
        product.setDetailOptionPrice(detailOptionPrice);
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return product;
    }
}
