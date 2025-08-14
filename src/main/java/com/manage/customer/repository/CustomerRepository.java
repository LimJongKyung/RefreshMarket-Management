package com.manage.customer.repository;

import com.manage.customer.entity.Customer;

import java.util.List;

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
}