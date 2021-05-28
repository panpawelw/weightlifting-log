package com.panpawelw.weightliftinglog.registration.listener;

import com.panpawelw.weightliftinglog.registration.event.OnRegistrationCompleteEvent;
import com.panpawelw.weightliftinglog.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private final EmailService emailService;

  private final VerificationTokenService verificationTokenService;

  @Autowired
  public RegistrationListener(EmailService emailService,
                              VerificationTokenService verificationTokenService) {
    this.emailService = emailService;
    this.verificationTokenService = verificationTokenService;
  }

  @Override
  public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
    final User user = event.getUser();
    VerificationToken verificationToken = new VerificationToken(user);
    verificationTokenService.saveToken(verificationToken);
    emailService.sendEmail(user.getEmail(), "support@panpawelw.com",
        "Weightlifting Log registration confirmation",
        "<h3>Welcome to Weightlifiting Log!</h3>" +
            "<p>You have registered an account on Weightlifting Log website. To verify your " +
            "account please click the link below within the next 24 hours:</p>" +
            "<a href=\"http://18.194.88.131:8080/weightliftinglog/confirm-account?token=\"" +
            verificationToken.getToken() +
            ">http://18.194.88.131:8080/weightliftinglog/confirm-account?token=" +
            verificationToken.getToken() + "</a>" + "<p>Have a nice day!!!</p>");
  }
}
