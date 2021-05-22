package com.panpawelw.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Value;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Value("${sendinblue.mail.apikey}")
  private String sendinblueApiKey;

  private final TransactionalEmailsApi api;

  @Autowired
  public EmailService(TransactionalEmailsApi api) {
    this.api = api;
  }

  public void sendEmail(String to, String from, String subject, String text) {
    ApiClient client = Configuration.getDefaultApiClient();
    // Configure API key authorization: api-key
    ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("api-key");
    auth.setApiKey(sendinblueApiKey);
    SendSmtpEmailSender sender = new SendSmtpEmailSender();
    sender.setEmail(from);
    List<SendSmtpEmailTo> toList = new ArrayList<>();
    SendSmtpEmailTo sendTo = new SendSmtpEmailTo();
    sendTo.setEmail(to);
    toList.add(sendTo);
    SendSmtpEmail email = new SendSmtpEmail();
    email.setSender(sender);
    email.setTo(toList);
    email.setHtmlContent(text);
    email.setSubject(subject);
    try {
      api.sendTransacEmail(email);
    } catch (ApiException e) {
      throw new RuntimeException("Error sending activation message!");
    }
  }
}
