package com.manage.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.manage.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query(value = """
	        SELECT 
	            TO_CHAR(order_date, 'YYYY-MM-DD') AS orderDate,
	            COUNT(NVL(customer_id, '비회원')) AS customerCount,
	            SUM(total_price) AS totalSum
	        FROM order_info
	        WHERE IS_CANCELED = 'N'
	        GROUP BY TO_CHAR(order_date, 'YYYY-MM-DD')
	        ORDER BY orderDate
	        """, nativeQuery = true)
	    List<Object[]> getOrderStatsRaw();
	    
		// 이름으로 검색
	    Page<Order> findByCustomerNameContaining(String customerName, Pageable pageable);

	    // 상태로 검색
	    Page<Order> findByOrderStatus(String orderStatus, Pageable pageable);

	    // 이름 + 상태 검색
	    Page<Order> findByCustomerNameContainingAndOrderStatus(String customerName, String orderStatus, Pageable pageable);
	    
	    @Query(value = """
	    	    WITH product_split AS (
	    	        SELECT
	    	            oi.ORDER_ID,
	    	            TO_CHAR(oi.ORDER_DATE, 'YYYY-MM-DD') AS orderDate,
	    	            REGEXP_SUBSTR(oi.PRODUCT_ID, '[^,]+', 1, LEVEL) AS productId,
	    	            TO_NUMBER(REGEXP_SUBSTR(oi.PRODUCT_QUANTITIES, '[^,]+', 1, LEVEL)) AS quantity
	    	        FROM order_info oi
	    	        WHERE oi.IS_CANCELED = 'N'
	    	        CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(oi.PRODUCT_ID, '[^,]+')) 
	    	            AND PRIOR ORDER_ID = ORDER_ID
	    	            AND PRIOR SYS_GUID() IS NOT NULL
	    	    )
	    	    SELECT
	    	        ps.orderDate,
	    	        p.NAME AS productName,
	    	        SUM(ps.quantity) AS totalQuantity
	    	    FROM product_split ps
	    	    JOIN products p ON p.PRODUCT_ID = ps.productId
	    	    GROUP BY ps.orderDate, p.NAME
	    	    ORDER BY ps.orderDate, totalQuantity DESC
	    	""", nativeQuery = true)
	    	List<Object[]> getDailyProductSales();
}