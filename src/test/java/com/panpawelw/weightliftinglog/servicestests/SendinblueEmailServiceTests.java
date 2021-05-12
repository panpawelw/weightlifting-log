package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.services.SendinblueEmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sendinblue.ApiException;
import sibApi.TransactionalEmailsApi;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SendinblueEmailServiceTests {

  @Mock
  private TransactionalEmailsApi api;

  private SendinblueEmailService service;

  @Before
  public void setup() {
    service = new SendinblueEmailService(api);
  }

  @Test
  public void testSendEmail() throws ApiException {
    service.sendEmail("test@to.com", "test@from.com",
        "Test subject", "Test message");
    verify(api).sendTransacEmail(any());
  }

  @Test
  public void testSendEmailThrowsException() throws ApiException {
    doThrow(ApiException.class).when(api).sendTransacEmail(any());

    try {
      service.sendEmail("test@to.com", "test@from.com",
          "Test subject", "Test message");
    } catch (RuntimeException e) {
      assertEquals(e.getMessage(), "Error sending activation message!");
    }
    verify(api).sendTransacEmail(any());
  }
}
