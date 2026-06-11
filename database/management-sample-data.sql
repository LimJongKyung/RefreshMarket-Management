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

COMMIT;
