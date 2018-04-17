package com.hungerfool.stockwatcher.email;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hungerfool.stockwatcher.EmailConfig;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailConfig emailConfig;
	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleMail(String sendTo, String titel, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfig.getEmailFrom());
		message.setTo(sendTo);
		message.setSubject(titel);
		message.setText(content);
		mailSender.send(message);
	}

	public void sendAttachmentsMail(String sendTo, String titel, String content, List<Map<String, File>> attachments) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(emailConfig.getEmailFrom());
			helper.setTo(sendTo);
			helper.setSubject(titel);
			helper.setText(content);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		mailSender.send(mimeMessage);
	}

	public void sendTemplateMail(String sendTo, String titel, Map<String, Object> content,
			List<Map<String, File>> attachments) {

	}

}
