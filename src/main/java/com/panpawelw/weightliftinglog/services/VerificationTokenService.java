package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;

import java.util.Date;
import java.util.Optional;

@Service
public class VerificationTokenService {

  private final VerificationTokenRepository verificationTokenRepository;
  private final UserService userService;

  @Autowired
  public VerificationTokenService(VerificationTokenRepository verificationTokenRepository,
      UserService userService) {
    this.verificationTokenRepository = verificationTokenRepository;
    this.userService = userService;
  }

  public VerificationToken findByUser(User user) {
    Optional<VerificationToken> verificationToken =
        verificationTokenRepository.findByUser(user);
    return verificationToken.orElse(null);
  }

  public VerificationToken findByToken(String token) {
    Optional<VerificationToken> verificationToken =
        verificationTokenRepository.findByToken(token);
    return verificationToken.orElse(null);
  }

  public void saveToken(VerificationToken verificationToken) {
    verificationTokenRepository.saveAndFlush(verificationToken);
  }

  public void deleteVerificationToken(VerificationToken verificationToken) {
    verificationTokenRepository.delete(verificationToken);
  }

  public String removeAccountIfTokenExpired(User user) {
    VerificationToken verificationToken = findByUser(user);
    if (verificationToken != null) {
      if (verificationToken.getExpiryDate().compareTo(new Date()) < 0) {
        deleteVerificationToken(verificationToken);
        userService.deleteUserById(user.getId());
        return "Activation time expired, user account removed!";
      }
      return "This account requires activation!";
    }
    return "";
  }
}