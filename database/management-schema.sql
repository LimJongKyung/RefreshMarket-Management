DECLARE
    column_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO column_count
    FROM user_tab_columns
    WHERE table_name = 'MEMBER'
      AND column_name = 'JOIN_DATE';

    IF column_count = 0 THEN
        EXECUTE IMMEDIATE
            'ALTER TABLE member ADD join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP';
    END IF;
END;
/

DECLARE
    column_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO column_count
    FROM user_tab_columns
    WHERE table_name = 'MEMBER'
      AND column_name = 'BENEFITS';

    IF column_count = 0 THEN
        EXECUTE IMMEDIATE
            'ALTER TABLE member ADD benefits VARCHAR2(1000)';
    END IF;
END;
/

DECLARE
    object_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO object_count
    FROM user_tables
    WHERE table_name = 'BENEFIT';

    IF object_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE benefit (
                name VARCHAR2(255) PRIMARY KEY,
                description VARCHAR2(1000)
            )';
    END IF;
END;
/

DECLARE
    object_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO object_count
    FROM user_tables
    WHERE table_name = 'ADMIN_PERMISSION';

    IF object_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE admin_permission (
                member_id VARCHAR2(50) PRIMARY KEY,
                perm_name VARCHAR2(500) NOT NULL,
                CONSTRAINT fk_admin_permission_member
                    FOREIGN KEY (member_id) REFERENCES member(id)
            )';
    END IF;
END;
/

DECLARE
    object_count NUMBER;
BEGIN
    FOR seq_record IN (
        SELECT 'MENU_SEQ' AS seq_name FROM dual
        UNION ALL SELECT 'PRODUCT_ID_SEQ' FROM dual
        UNION ALL SELECT 'PRODUCT_IMAGE_SEQ' FROM dual
        UNION ALL SELECT 'PRODUCT_DETAIL_IMAGE_SEQ' FROM dual
    ) LOOP
        SELECT COUNT(*) INTO object_count
        FROM user_sequences
        WHERE sequence_name = seq_record.seq_name;

        IF object_count = 0 THEN
            EXECUTE IMMEDIATE
                'CREATE SEQUENCE ' || seq_record.seq_name ||
                ' START WITH 100 INCREMENT BY 1 NOCACHE';
        END IF;
    END LOOP;
END;
/

UPDATE member
SET join_date = COALESCE(join_date, CURRENT_TIMESTAMP);

COMMIT;
