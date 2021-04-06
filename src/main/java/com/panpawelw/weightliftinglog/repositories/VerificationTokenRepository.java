package com.panpawelw.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

  Optional<VerificationToken> findByToken(String token);

  Optional<VerificationToken> findByUser(User user);
}