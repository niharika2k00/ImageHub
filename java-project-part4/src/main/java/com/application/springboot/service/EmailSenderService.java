package com.application.springboot.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

  private final JavaMailSender javaMailSender;

  @Value("${custom.email}")
  private String senderEmail;

  @Autowired
  public EmailSenderService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendEmail(String payload) throws ParseException, MessagingException {
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    String receiverEmail = (String) jsonObj.get("receiverEmail");
    String subject = (String) jsonObj.get("subject");
    String text = (String) jsonObj.get("body");

    // Create a MimeMessage and use MimeMessageHelper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
    message.setSubject(subject);
    message.setText(text, true); // enable HTML content
    message.setTo(receiverEmail);
    message.setFrom(senderEmail);

    // Working code but only supports TXT, not HTML/MD
    // SimpleMailMessage message = new SimpleMailMessage();
    // message.setSubject(subject);
    // message.setText(text);
    // message.setTo(receiverEmail);
    // message.setFrom(senderEmail);

    javaMailSender.send(mimeMessage);
  }
}
