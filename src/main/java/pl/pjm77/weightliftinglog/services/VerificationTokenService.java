package pl.pjm77.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.VerificationToken;
import pl.pjm77.weightliftinglog.repositories.VerificationTokenRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository, UserService userService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userService = userService;
    }

    public VerificationToken findByUser(User user) {
        Optional<VerificationToken> verificationToken =
                verificationTokenRepository.findByUser(user);
        return verificationToken.orElse(null);
    }

    public void saveToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public void deleteVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    public String removeAccountIfTokenExpired(User user) {
        VerificationToken verificationToken = findByUser(user);
        if (verificationToken != null) {
            if(verificationToken.getExpiryDate().compareTo(new Date()) < 0) {
                deleteVerificationToken(verificationToken);
                userService.deleteUserById(user.getId());
                return "Activation time expired, user account removed!";
            }
            return "This account requires activation!";
        }
        return "";
    }
}