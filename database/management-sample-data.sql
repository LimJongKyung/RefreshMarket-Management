MERGE INTO benefit target
USING (
    SELECT '무료 배송' AS name, '관리자 페이지 확인용 샘플 혜택' AS description
    FROM dual
) source
ON (target.name = source.name)
WHEN NOT MATCHED THEN
    INSERT (name, description)
    VALUES (source.name, source.description);

MERGE INTO admin_permission target
USING (
    SELECT
        'portfolio' AS member_id,
        '회원 관리,상품 관리,메뉴 관리,관리자 계정 관리,할인 정책 설정' AS perm_name
    FROM dual
) source
ON (target.member_id = source.member_id)
WHEN MATCHED THEN
    UPDATE SET target.perm_name = source.perm_name
WHEN NOT MATCHED THEN
    INSERT (member_id, perm_name)
    VALUES (source.member_id, source.perm_name);

UPDATE member
SET grade = '운영자'
WHERE id = 'portfolio';

MERGE INTO products target
USING (
    SELECT
        9101 AS product_id,
        '제주 햇감귤 3kg' AS name,
        '상큼한 산지 직송 감귤 샘플 상품입니다. 이미지는 상품 수정 화면에서 업로드해 주세요.' AS description,
        15900 AS price,
        120 AS stock,
        '과일' AS category,
        'sale' AS main_display,
        'RefreshMarket 산지직송' AS seller,
        '제주 감귤 협동조합' AS manufacturer,
        '기본 3kg, 선물 포장' AS detail_option,
        '0,3000' AS detail_option_price
    FROM dual
) source
ON (target.product_id = source.product_id)
WHEN MATCHED THEN
    UPDATE SET
        target.name = source.name,
        target.description = source.description,
        target.price = source.price,
        target.stock = source.stock,
        target.category = source.category,
        target.main_display = source.main_display,
        target.seller = source.seller,
        target.manufacturer = source.manufacturer,
        target.detail_option = source.detail_option,
        target.detail_option_price = source.detail_option_price
WHEN NOT MATCHED THEN
    INSERT (
        product_id,
        name,
        description,
        price,
        stock,
        category,
        main_display,
        seller,
        manufacturer,
        detail_option,
        detail_option_price,
        created_at,
        updated_at
    )
    VALUES (
        source.product_id,
        source.name,
        source.description,
        source.price,
        source.stock,
        source.category,
        source.main_display,
        source.seller,
        source.manufacturer,
        source.detail_option,
        source.detail_option_price,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

MERGE INTO products target
USING (
    SELECT
        9102 AS product_id,
        '강원 고랭지 감자 5kg' AS name,
        '포슬포슬한 식감의 고랭지 감자 샘플 상품입니다. 대표 이미지를 직접 등록할 수 있습니다.' AS description,
        12900 AS price,
        85 AS stock,
        '채소' AS category,
        'group' AS main_display,
        'RefreshMarket 산지직송' AS seller,
        '강원 로컬푸드' AS manufacturer,
        '기본 5kg, 대용량 10kg' AS detail_option,
        '0,9000' AS detail_option_price
    FROM dual
) source
ON (target.product_id = source.product_id)
WHEN MATCHED THEN
    UPDATE SET
        target.name = source.name,
        target.description = source.description,
        target.price = source.price,
        target.stock = source.stock,
        target.category = source.category,
        target.main_display = source.main_display,
        target.seller = source.seller,
        target.manufacturer = source.manufacturer,
        target.detail_option = source.detail_option,
        target.detail_option_price = source.detail_option_price
WHEN NOT MATCHED THEN
    INSERT (
        product_id,
        name,
        description,
        price,
        stock,
        category,
        main_display,
        seller,
        manufacturer,
        detail_option,
        detail_option_price,
        created_at,
        updated_at
    )
    VALUES (
        source.product_id,
        source.name,
        source.description,
        source.price,
        source.stock,
        source.category,
        source.main_display,
        source.seller,
        source.manufacturer,
        source.detail_option,
        source.detail_option_price,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

MERGE INTO products target
USING (
    SELECT
        9103 AS product_id,
        '국산 잡곡 10종 세트' AS name,
        '현미, 보리, 수수 등 자주 쓰는 잡곡으로 구성한 샘플 상품입니다.' AS description,
        21900 AS price,
        64 AS stock,
        '잡곡' AS category,
        'promotion' AS main_display,
        'RefreshMarket 큐레이션' AS seller,
        '우리곡물영농조합' AS manufacturer,
        '소포장, 가족팩' AS detail_option,
        '0,6000' AS detail_option_price
    FROM dual
) source
ON (target.product_id = source.product_id)
WHEN MATCHED THEN
    UPDATE SET
        target.name = source.name,
        target.description = source.description,
        target.price = source.price,
        target.stock = source.stock,
        target.category = source.category,
        target.main_display = source.main_display,
        target.seller = source.seller,
        target.manufacturer = source.manufacturer,
        target.detail_option = source.detail_option,
        target.detail_option_price = source.detail_option_price
WHEN NOT MATCHED THEN
    INSERT (
        product_id,
        name,
        description,
        price,
        stock,
        category,
        main_display,
        seller,
        manufacturer,
        detail_option,
        detail_option_price,
        created_at,
        updated_at
    )
    VALUES (
        source.product_id,
        source.name,
        source.description,
        source.price,
        source.stock,
        source.category,
        source.main_display,
        source.seller,
        source.manufacturer,
        source.detail_option,
        source.detail_option_price,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

MERGE INTO products target
USING (
    SELECT
        9104 AS product_id,
        '프리미엄 토마토 2kg' AS name,
        '샐러드와 주스에 모두 쓰기 좋은 토마토 샘플 상품입니다.' AS description,
        17900 AS price,
        98 AS stock,
        '채소' AS category,
        'sale' AS main_display,
        'RefreshMarket 산지직송' AS seller,
        '스마트팜 토마토팜' AS manufacturer,
        '일반, 완숙' AS detail_option,
        '0,1500' AS detail_option_price
    FROM dual
) source
ON (target.product_id = source.product_id)
WHEN MATCHED THEN
    UPDATE SET
        target.name = source.name,
        target.description = source.description,
        target.price = source.price,
        target.stock = source.stock,
        target.category = source.category,
        target.main_display = source.main_display,
        target.seller = source.seller,
        target.manufacturer = source.manufacturer,
        target.detail_option = source.detail_option,
        target.detail_option_price = source.detail_option_price
WHEN NOT MATCHED THEN
    INSERT (
        product_id,
        name,
        description,
        price,
        stock,
        category,
        main_display,
        seller,
        manufacturer,
        detail_option,
        detail_option_price,
        created_at,
        updated_at
    )
    VALUES (
        source.product_id,
        source.name,
        source.description,
        source.price,
        source.stock,
        source.category,
        source.main_display,
        source.seller,
        source.manufacturer,
        source.detail_option,
        source.detail_option_price,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

COMMIT;
