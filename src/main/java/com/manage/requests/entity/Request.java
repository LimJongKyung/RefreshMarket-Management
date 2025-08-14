package com.manage.requests.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스에 맞게 수정 가능
    @Column(name = "REQUEST_ID")
    private Long requestId;

    @Column(name = "CONSULTANT_NAME", length = 100, nullable = false)
    private String consultantName;

    @Column(name = "CONSULTANT_EMAIL", length = 100, nullable = false)
    private String consultantEmail;

    @Column(name = "CONSULTATION_TYPE", length = 50, nullable = false)
    private String consultationType;

    @Column(name = "CONSULTANT_PASSWORD", length = 255, nullable = false)
    private String consultantPassword;

    @Lob
    @Column(name = "CONSULTANT_MESSAGE", nullable = false)
    private String consultantMessage;

    @Column(name = "REQUEST_DATE")
    private LocalDateTime requestDate;

    // 기본 생성자
    public Request() {
    }

    // 게터, 세터
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getConsultantName() { return consultantName; }
    public void setConsultantName(String consultantName) { this.consultantName = consultantName; }

    public String getConsultantEmail() { return consultantEmail; }
    public void setConsultantEmail(String consultantEmail) { this.consultantEmail = consultantEmail; }

    public String getConsultationType() { return consultationType; }
    public void setConsultationType(String consultationType) { this.consultationType = consultationType; }

    public String getConsultantPassword() { return consultantPassword; }
    public void setConsultantPassword(String consultantPassword) { this.consultantPassword = consultantPassword; }

    public String getConsultantMessage() { return consultantMessage; }
    public void setConsultantMessage(String consultantMessage) { this.consultantMessage = consultantMessage; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
}