package com.panpawelw.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AWSEmailService implements EmailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public AWSEmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public void sendEmail(String to, String from, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setFrom(from);
    message.setSubject(subject);
    message.setText(text);
    javaMailSender.send(message);
  }
}
