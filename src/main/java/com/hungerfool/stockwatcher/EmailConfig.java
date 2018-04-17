package com.hungerfool.stockwatcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class EmailConfig {

	/**
	 * 发件邮箱
	 */
	@Value("${spring.mail.username}")
	private String emailFrom;

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

}