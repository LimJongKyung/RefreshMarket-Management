package com.manage.mail.service;

public interface MailService {
	void sendIdEmail(String toEmail, String userId);
	void sendTempPasswordEmail(String toEmail, String tempPassword);
}
