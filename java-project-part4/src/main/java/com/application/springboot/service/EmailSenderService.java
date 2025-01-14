package com.application.springboot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

  public void sendEmail(String payload) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    String receiverEmail = (String) jsonObj.get("receiverEmail");
    String text = (String) jsonObj.get("text");
    String subject = (String) jsonObj.get("subject");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setText(text);
    message.setSubject(subject);
    message.setTo("niharika.2k00@gmail.com");
    message.setFrom(senderEmail);

    javaMailSender.send(message);
  }
}
