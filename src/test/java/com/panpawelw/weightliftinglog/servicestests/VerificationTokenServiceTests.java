package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;
import com.panpawelw.weightliftinglog.repositories.VerificationTokenRepository;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceTests {

  private static final User TEST_USER = new User(12L, "Test user", "Test password",
      "Test password", "test@email.com", true);

  private static final VerificationToken TEST_VERIFICATION_TOKEN =
      new VerificationToken(TEST_USER);

  @Mock
  private VerificationTokenRepository repository;

  @Mock
  private UserService userService;

  private VerificationTokenService service;

  @Before
  public void setup() {
    service = new VerificationTokenService(repository, userService);
  }

  @Test
  public void testFindByUser() {
    User user = new User();
    when(repository.findByUser(user)).thenReturn(java.util.Optional.of(TEST_VERIFICATION_TOKEN));

    assertEquals(service.findByUser(user), TEST_VERIFICATION_TOKEN, "Tokens don't match!");
    verify(repository).findByUser(user);
  }

  @Test
  public void testFindByToken() {
    when(repository.findByToken(TEST_VERIFICATION_TOKEN.getToken()))
        .thenReturn(java.util.Optional.of(TEST_VERIFICATION_TOKEN));

    assertEquals(service.findByToken(TEST_VERIFICATION_TOKEN.getToken())
        , TEST_VERIFICATION_TOKEN, "Tokens don't match!");
    verify(repository).findByToken(TEST_VERIFICATION_TOKEN.getToken());
  }

  @Test
  public void testSaveToken() {
    VerificationToken TEST_VERIFICATION_TOKEN = new VerificationToken();
    service.saveToken(TEST_VERIFICATION_TOKEN);

    verify(repository).saveAndFlush(TEST_VERIFICATION_TOKEN);
  }

  @Test
  public void testDeleteVerificationToken() {
    service.deleteVerificationToken(TEST_VERIFICATION_TOKEN);

    verify(repository).delete(TEST_VERIFICATION_TOKEN);
  }

  @Test
  public void testRemoveAccountIfTokenExpiredWhenTokenNotExpired() {
    when(repository.findByUser(TEST_USER))
        .thenReturn(java.util.Optional.of(TEST_VERIFICATION_TOKEN));

    assertEquals(service.removeAccountIfTokenExpired(TEST_USER),
        "This account requires activation!");
    verify(repository).findByUser(TEST_USER);
  }

  @Test
  public void testRemoveAccountIfTokenExpiredWhenTokenExpired() {
    VerificationToken expiredVerificationToken = new VerificationToken(TEST_USER);
    expiredVerificationToken.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
    when(repository.findByUser(TEST_USER))
        .thenReturn(java.util.Optional.of(expiredVerificationToken));

    assertEquals(service.removeAccountIfTokenExpired(TEST_USER),
        "Activation time expired, user account removed!");
    verify(repository).findByUser(TEST_USER);
  }

  @Test
  public void testRemoveAccountIfTokenExpiredWhenTokenDoesNotExist() {
    when(repository.findByUser(any())).thenReturn(Optional.empty());

    assertEquals(service.removeAccountIfTokenExpired(TEST_USER),
        "");
  }
}
