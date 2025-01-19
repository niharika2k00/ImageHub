package com.application.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    //https://www.baeldung.com/spring-email
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    // SMTP server configuration
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587); // TLS
    mailSender.setUsername("dniharika16@gmail.com");
    mailSender.setPassword("dstz lifj eznn gslj");
    //mailSender.setPassword("Sneha*12");

    // Additional mail properties
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.debug", "false");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.required", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.transport.protocol", "smtp");

    return mailSender;
  }
}

/*
  SMTP Server setup :
    1. Goto "Manage your Google Account"
    2. Then "Security" -> "2-Step Verification" -> "App Password"
    3. If "App Password" doesn't exist then search in the search bar
*/
