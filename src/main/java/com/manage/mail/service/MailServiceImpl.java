package com.manage.mail.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

	private final JavaMailSender mailSender;
	
	public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

	@Override
    public void sendIdEmail(String toEmail, String userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("아이디 찾기 결과");
        message.setText("요청하신 아이디는 다음과 같습니다: " + userId);
        mailSender.send(message);
    }
	
	@Override
	public void sendTempPasswordEmail(String toEmail, String tempPassword) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(toEmail);
	    message.setSubject("임시 비밀번호 안내");
	    message.setText("요청하신 임시 비밀번호는 다음과 같습니다: " + tempPassword + "\n로그인 후 반드시 비밀번호를 변경해주세요.");
	    mailSender.send(message);
	}
}
