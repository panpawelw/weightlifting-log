package pl.pjm77.weightliftinglog.registration.event;

import org.springframework.context.ApplicationEvent;
import pl.pjm77.weightliftinglog.models.User;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final User user;
    private final String appUrl;
    private final Locale locale;

    public OnRegistrationCompleteEvent(final User user, final String appUrl, final Locale locale) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }
}
