package pl.pjm77.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.VerificationToken;
import pl.pjm77.weightliftinglog.repositories.VerificationTokenRepository;

import java.util.Date;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void saveToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public String removeAccountIfTokenExpired(User user) {
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
        if (verificationToken != null) {
            if(verificationToken.getExpiryDate().compareTo(new Date()) < 0) {
                return "Activation time expired, user account removed!";
            }
            return "This account requires activation!";
        }
        return "";
    }
}