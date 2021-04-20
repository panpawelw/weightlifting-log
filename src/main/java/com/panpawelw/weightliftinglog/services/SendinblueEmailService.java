package com.panpawelw.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Value;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendinblueEmailService implements EmailService {

  @Autowired
  public SendinblueEmailService() {}

  @Value("${sendinblue.mail.apikey}")
  private String sendinblueApiKey;

  @Override
  public void sendEmail(String to, String from, String subject, String text) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    // Configure API key authorization: api-key
    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(sendinblueApiKey);

    try {
      TransactionalEmailsApi api = new TransactionalEmailsApi();
      SendSmtpEmailSender sender = new SendSmtpEmailSender();
      sender.setEmail(from);
      List<SendSmtpEmailTo> toList = new ArrayList<>();
      SendSmtpEmailTo sendTo = new SendSmtpEmailTo();
      sendTo.setEmail(to);
      toList.add(sendTo);
      SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
      sendSmtpEmail.setSender(sender);
      sendSmtpEmail.setTo(toList);
      sendSmtpEmail.setHtmlContent(text);
      sendSmtpEmail.setSubject(subject);
      CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.out.println("Exception occurred:- " + e.getMessage());
    }
  }
}
