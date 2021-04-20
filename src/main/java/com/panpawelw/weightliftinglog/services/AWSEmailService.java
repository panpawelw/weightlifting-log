package com.panpawelw.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AWSEmailService implements EmailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public AWSEmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public void sendEmail(String to, String from, String subject, String text) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      helper.setText(text, true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }

    javaMailSender.send(message);
  }
}
