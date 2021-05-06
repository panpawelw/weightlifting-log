package com.panpawelw.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AWSEmailService implements EmailService {

  private final JavaMailSender sender;

  @Autowired
  public AWSEmailService(JavaMailSender sender) {
    this.sender = sender;
  }

  @Override
  public void sendEmail(String to, String from, String subject, String text) {
    MimeMessage message = sender.createMimeMessage();
    try {
      MimeMessageHelper helper = getHelper(message);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);
      helper.setText(text, true);
    } catch (MessagingException e) {
      throw new RuntimeException("Error sending activation message!");
    }

    sender.send(message);
  }

  protected MimeMessageHelper getHelper(MimeMessage message) throws MessagingException {
    return new MimeMessageHelper(message, true);
  }
}
