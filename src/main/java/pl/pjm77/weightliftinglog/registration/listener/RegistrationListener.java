package pl.pjm77.weightliftinglog.registration.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.registration.event.OnRegistrationCompleteEvent;
import pl.pjm77.weightliftinglog.services.EmailService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private EmailService emailService;

    @Autowired
    public RegistrationListener (final EmailService emailService){
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        emailService.sendEmail(user.getEmail(), "Weightlifting Log registration" +
                " confirmation", "You have registered an account on Weightlifting Log " +
                "website. Have a nice day!");
    }
}
