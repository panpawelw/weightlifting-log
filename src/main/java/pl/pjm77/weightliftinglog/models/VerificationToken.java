package pl.pjm77.weightliftinglog.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Verification token entity represents a verification token generated when user
 * chooses to use real email account and receive a confirmation email. Token is
 * generated and given 24 hour expiry date.
 *
 * id - database Id
 * token - string containing generated token
 * user - user this token belongs to
 * expiryDate - date when token loses it's validity
  */
@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn
    private User user;

    private Date expiryDate;

    public VerificationToken() {
        this.token = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate();
    }

    public VerificationToken(final User user) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Calculates a date 24 from now when token is no longer valid
     * @return expiry date
     */
    private Date calculateExpiryDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.HOUR, 24);
        return new Date(calendar.getTime().getTime());
    }
}
