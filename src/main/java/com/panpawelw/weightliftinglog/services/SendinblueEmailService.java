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
public class SendinblueEmailService implements EmailService {

  private final TransactionalEmailsApi api;

  private final SendSmtpEmailSender sender;

  private final SendSmtpEmailTo sendTo;

  private final SendSmtpEmail email;

  @Autowired
  public SendinblueEmailService(TransactionalEmailsApi api, SendSmtpEmailSender sender,
      SendSmtpEmailTo sendTo, SendSmtpEmail email) {
    this.api = api;
    this.sender = sender;
    this.sendTo = sendTo;
    this.email = email;
  }

  @Value("${sendinblue.mail.apikey}")
  private String sendinblueApiKey;

  @Override
  public void sendEmail(String to, String from, String subject, String text) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    // Configure API key authorization: api-key
    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(sendinblueApiKey);
    sender.setEmail(from);
    List<SendSmtpEmailTo> toList = new ArrayList<>();
    sendTo.setEmail(to);
    toList.add(sendTo);
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
