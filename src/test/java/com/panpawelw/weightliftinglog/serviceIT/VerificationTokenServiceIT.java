package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.VerificationToken;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerificationTokenServiceIT {

  @Autowired
  private VerificationTokenService service;

  @Autowired
  private UserService userService;

  @Test
  public void findByUserShouldSucceed() {
    User testUser = userService.findUserByEmail("token@test1.com");

    assertEquals("Test token 1", service.findByUser(testUser).getToken());
  }

  @Test
  public void findByUserShouldReturnNull() {
    User testUser = userService.findUserByEmail("token@test2.com");

    assertNull(service.findByUser(testUser));
  }

  @Test
  public void findByTokedShouldSucceed() {
    assertEquals("Test token 1", service.findByToken("Test token 1").getToken());
  }

  @Test
  public void findByTokenShouldReturnNull() {
    assertNull(service.findByToken("Not existing token"));
  }

  @Test
  public void saveTokenShouldSucceed() {
    User user = userService.findUserByEmail("token@test3.com");
    VerificationToken token = new VerificationToken(user);
    service.saveToken(token);

    assertNotNull(service.findByUser(user));
  }

  @Test
  public void deleteTokenShouldSucceed() {
    User user = userService.findUserByEmail("token@test4.com");
    VerificationToken token = service.findByUser(user);

    service.deleteVerificationToken(token);
    assertNull(service.findByUser(user));
  }

  @Test
  public void deleteTokenShouldFail() {
    User user = userService.findUserByEmail("token@test5");
    VerificationToken token = new VerificationToken(user);

    service.deleteVerificationToken(token);
    assertNull(service.findByUser(user));
  }

  @Test
  public void removeAccountIfTokenExpiredShouldDoNothing() {
    long initialDatabaseCount = service.count();
    User user = userService.findUserByEmail("token@test6.com");

    assertEquals("", service.removeAccountIfTokenExpired(user));
    assertEquals(0, service.count() - initialDatabaseCount);
  }

  @Test
  public void removeAccountIfTokenExpiredShouldRemindOfActivation() {
    User user = userService.findUserByEmail("token@test7.com");

    assertEquals("This account requires activation!",
        service.removeAccountIfTokenExpired(user));
    assertNotNull(service.findByUser(user));
  }

  @Test
  public void removeAccountIfTokenExpiredShouldRemoveUserAndToken() {
    User user = userService.findUserByEmail("token@test8.com");

    assertEquals("Activation time expired, user account removed!",
        service.removeAccountIfTokenExpired(user));
    assertNull(service.findByToken("Test token 4"));
    assertNull(userService.findUserByEmail("token@test8.com"));
  }
}
