package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.services.SendinblueEmailService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

public class SendinblueEmailServiceTests {

  @Mock
  private TransactionalEmailsApi api;

  @Mock
  private SendSmtpEmailSender sender;

  @Mock
  private SendSmtpEmailTo sendTo;

  @Mock
  private SendSmtpEmail email;

  private SendinblueEmailService service;

  @Before
  public void setup() {
    service = new SendinblueEmailService(api, sender, sendTo, email);
  }

  @Test
  public void testSendEmail() {
  }

  @Test
  public void testSendEmailThrowsException() {
  }
}
