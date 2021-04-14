package com.panpawelw.weightliftinglog.services;

public interface EmailService {
  void sendEmail(String to, String from, String subject, String text);
}
