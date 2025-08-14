package com.manage.requests.service;

import com.manage.requests.entity.Request;
import com.manage.requests.repository.RequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Page<Request> getRequests(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return requestRepository.findAll(pageable);
        } else {
            return requestRepository.findByConsultantNameContainingIgnoreCaseOrConsultantEmailContainingIgnoreCase(keyword, keyword, pageable);
        }
    }
    
    @Override
    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 요청이 존재하지 않습니다. id=" + id));
    }
}
