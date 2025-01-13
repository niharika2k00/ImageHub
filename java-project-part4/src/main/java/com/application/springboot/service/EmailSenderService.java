package com.application.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public EmailSenderService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendEmail() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setText("We are pleased to inform you that your image has been successfully converted into all resolutions. Now the files are ready for use.");
    message.setSubject("Your Image Has Been Successfully Converted!");
    message.setFrom("dniharika16@gmail.com");
    message.setTo("niharika.2k00@gmail.com");
    //message.setTo("bh.kaito@gmail.com");

    javaMailSender.send(message);
  }

}
