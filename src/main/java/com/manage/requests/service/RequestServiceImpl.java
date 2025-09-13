package com.manage.requests.service;

import com.manage.requests.entity.Request;
import com.manage.requests.repository.RequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 추가

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    @Transactional(readOnly = true) // 조회 메서드에는 readOnly = true 옵션을 주는 것이 성능에 유리합니다.
    public Page<Request> getRequests(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return requestRepository.findAll(pageable);
        } else {
            return requestRepository.findByConsultantNameContainingIgnoreCaseOrConsultantEmailContainingIgnoreCase(keyword, keyword, pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청이 존재하지 않습니다. id=" + id));
    }

    @Override
    @Transactional
    public void saveAnswer(Long id, String answer) {
        Request request = getRequestById(id);
        request.setAnswer(answer);
        requestRepository.save(request); // save 호출 선택적
    }

    @Override
    @Transactional
    public void deleteAnswer(Long id) {
        Request request = getRequestById(id);
        request.setAnswer(null);
        requestRepository.save(request);
    }
}