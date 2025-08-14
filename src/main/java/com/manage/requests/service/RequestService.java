package com.manage.requests.service;

import com.manage.requests.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Page<Request> getRequests(String keyword, Pageable pageable);
    Request getRequestById(Long id);
}