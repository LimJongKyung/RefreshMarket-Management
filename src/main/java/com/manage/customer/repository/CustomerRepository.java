package com.manage.customer.repository;

import com.manage.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	Page<Customer> findByAddressContainingIgnoreCase(String address, Pageable pageable);
	Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
	Page<Customer> findByIdContainingIgnoreCase(String id, Pageable pageable);
	
	@Query(value = "SELECT TO_CHAR(JOIN_DATE, 'YYYY-MM-DD') AS day, COUNT(*) AS count " +
            "FROM MEMBER " +
            "GROUP BY TO_CHAR(JOIN_DATE, 'YYYY-MM-DD') " +
            "ORDER BY day DESC", nativeQuery = true)
    List<Object[]> countNewMembersPerDayRaw();
    
    @Query(value = "SELECT TO_CHAR(REQUEST_DATE, 'YYYY-MM-DD') AS day, COUNT(*) AS count " +
            "FROM REQUESTS " +
            "GROUP BY TO_CHAR(REQUEST_DATE, 'YYYY-MM-DD') " +
            "ORDER BY day DESC", nativeQuery = true)
    List<Object[]> countRequestsPerDayRaw();
    
    List<Customer> findByGradeIn(List<String> grade);
    
    Customer findByIdAndPasswd(String id, String passwd); // 로그인 로직
    
    @Query(value = """
    	    SELECT
    	        CASE 
    	            WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('1','3') THEN '남자'
    	            WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('2','4') THEN '여자'
    	            ELSE '기타'
    	        END AS gender,
    	        EXTRACT(YEAR FROM SYSDATE) - 
    	            CASE 
    	                WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('1','2') 
    	                    THEN 1900 + TO_NUMBER(SUBSTR(m.REGISTRATIONNUMBER,1,2))
    	                WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('3','4') 
    	                    THEN 2000 + TO_NUMBER(SUBSTR(m.REGISTRATIONNUMBER,1,2))
    	                ELSE 0
    	            END AS age,
    	        COUNT(DISTINCT oi.ORDER_ID) AS orderCount
    	    FROM order_info oi
    	    JOIN member m ON oi.CUSTOMER_ID = m.ID
    	    WHERE oi.IS_CANCELED = 'N'
    	    GROUP BY
    	        CASE 
    	            WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('1','3') THEN '남자'
    	            WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('2','4') THEN '여자'
    	            ELSE '기타'
    	        END,
    	        EXTRACT(YEAR FROM SYSDATE) - 
    	            CASE 
    	                WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('1','2') 
    	                    THEN 1900 + TO_NUMBER(SUBSTR(m.REGISTRATIONNUMBER,1,2))
    	                WHEN SUBSTR(m.REGISTRATIONNUMBER, INSTR(m.REGISTRATIONNUMBER,'-')+1, 1) IN ('3','4') 
    	                    THEN 2000 + TO_NUMBER(SUBSTR(m.REGISTRATIONNUMBER,1,2))
    	                ELSE 0
    	            END
    	    ORDER BY gender, age
    	""", nativeQuery = true)
    	List<Object[]> getGenderAgeOrderStats();
    	
    	boolean existsById(String id);
    	
    Optional<Customer> findByNameAndEmail(String name, String email);
    
    Optional<Customer> findByIdAndName(String id, String name);
}