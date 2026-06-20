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
	            COUNT(COALESCE(customer_id, '비회원')) AS customerCount,
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
		            CAST(NULLIF(TRIM(product_ids.product_id), '') AS BIGINT) AS productId,
		            CAST(NULLIF(TRIM(quantities.quantity), '') AS BIGINT) AS quantity
		        FROM order_info oi
		        CROSS JOIN LATERAL unnest(string_to_array(oi.PRODUCT_ID, ',')) WITH ORDINALITY AS product_ids(product_id, ord)
		        LEFT JOIN LATERAL unnest(string_to_array(oi.PRODUCT_QUANTITIES, ',')) WITH ORDINALITY AS quantities(quantity, ord)
		            ON quantities.ord = product_ids.ord
		        WHERE oi.IS_CANCELED = 'N'
		    )
		    SELECT
		        ps.orderDate,
		        p.NAME AS productName,
		        SUM(COALESCE(ps.quantity, 0)) AS totalQuantity
		    FROM product_split ps
		    JOIN products p ON p.PRODUCT_ID = ps.productId
		    GROUP BY ps.orderDate, p.NAME
		    ORDER BY ps.orderDate, totalQuantity DESC
		""", nativeQuery = true)
		List<Object[]> getDailyProductSales();
}
