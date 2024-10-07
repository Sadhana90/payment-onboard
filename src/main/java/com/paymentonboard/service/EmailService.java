package com.paymentonboard.service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.paymentonboard.dto.Mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private Configuration freemarkerConfig;

	@Value("${mailFrom}")
	private String mailFrom;

	@Value("${emailTemplateOTPSignUp}")
	private String emailTemplateOTPSignUp;

	@Value("${mailOTPSubject}")
	private String mailOTPSubject;

	@Value("${emailTemplateUserCreation}")
	private String emailTemplateUserCreation;

	@Value("${mailUserCreationSubject}")
	private String mailUserCreationSubject;


	
	public void selfonboardEmail(String userEmail, String fullname, String Otp)
			throws MessagingException, IOException, TemplateException {
		Mail mail = new Mail();
		log.info(mailFrom);
		mail.setFrom(mailFrom);
		mail.setSubject(mailOTPSubject);
		String urlReset = null;

		mail.setFrom(mailFrom);
		Map model = new HashMap();
		model.put("fullname", fullname);
		model.put("otp", Otp);

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		Template t = null;
		t = freemarkerConfig.getTemplate(emailTemplateOTPSignUp);
		mail.setTo(userEmail);
		mail.setModel(model);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		helper.addInline("logo.png", new ClassPathResource("logo.png"));
		emailSender.send(message);

	}

	public void selfonboardUserCreation(String userEmail, String fullname, String mid)
			throws MessagingException, IOException, TemplateException {
		Mail mail = new Mail();
		log.info(mailFrom);
		mail.setFrom(mailFrom);
		mail.setSubject(mailUserCreationSubject);
		String urlReset = null;

		mail.setFrom(mailFrom);
		Map model = new HashMap();
		model.put("fullname", fullname);
		model.put("MID", mid);

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		Template t = null;
		t = freemarkerConfig.getTemplate(emailTemplateUserCreation);
		mail.setTo(userEmail);
		mail.setModel(model);
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		helper.addInline("logo.png", new ClassPathResource("logo.png"));
		emailSender.send(message);

	}

}