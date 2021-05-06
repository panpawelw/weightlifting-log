package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.services.AWSEmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AWSEmailServiceTests {

  @Mock
  private JavaMailSender sender;

  @Mock
  private MimeMessage message;

  @Mock
  private MimeMessageHelper helper;

  @InjectMocks
  private AWSEmailService service;

  // Inner class to enable mocking MimeMessageHelper
  protected class TestableAWSEmailService extends AWSEmailService {

    public TestableAWSEmailService(JavaMailSender sender) {
      super(sender);
    }

    @Override
    protected MimeMessageHelper getHelper(MimeMessage message) {
      return helper;
    }
  }

  @Before
  public void setup() {
    service = new TestableAWSEmailService(sender);
  }

  @Test
  public void testSendEmail() {
    when(sender.createMimeMessage()).thenReturn(message);

    service.sendEmail("test@to.com", "test@from.com",
        "Test subject", "Test message");
    verify(sender).send(message);
  }

  @Test(expected = RuntimeException.class)
  public void testSendEmailThrowsMessagingException() throws MessagingException {
    when(sender.createMimeMessage()).thenReturn(message);
    doThrow(MessagingException.class).when(helper).setTo(anyString());

    service.sendEmail("test@to.com", "test@from.com",
        "Test subject", "Test message");
  }
}
