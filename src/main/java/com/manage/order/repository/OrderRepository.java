package com.manage.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
