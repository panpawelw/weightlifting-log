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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceTests {

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
    VerificationToken verificationToken = new VerificationToken();
    when(repository.findByUser(user)).thenReturn(java.util.Optional.of(verificationToken));

    assertEquals(service.findByUser(user), verificationToken, "Tokens don't match!");
    verify(repository).findByUser(user);
  }

  @Test
  public void testFindByToken() {
    VerificationToken verificationToken = new VerificationToken();
    when(repository.findByToken("fdsgagdsagfdsfd"))
        .thenReturn(java.util.Optional.of(verificationToken));

    assertEquals(service.findByToken("fdsgagdsagfdsfd"),
        verificationToken, "Tokens don't match!");
    verify(repository).findByToken("fdsgagdsagfdsfd");
  }

  @Test
  public void testSaveToken() {
    VerificationToken verificationToken = new VerificationToken();
    service.saveToken(verificationToken);
    verify(repository).saveAndFlush(verificationToken);
  }

  @Test
  public void testDeleteVerificationToken() {
    VerificationToken verificationToken = new VerificationToken();
    service.deleteVerificationToken(verificationToken);

    verify(repository).delete(verificationToken);
  }

  @Test
  public void testRemoveAccountIfTokenExpired() {

  }
}
