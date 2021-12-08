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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerificationTokenServiceIT {

  @Autowired
  private VerificationTokenService service;

  @Autowired
  private UserService userService;

  @Test
  public void findByUserShouldSucceed() {
    User testUser = userService.findUserByEmail("test@email1.com");

    assertEquals("Test token 1", service.findByUser(testUser).getToken());
  }

  @Test
  public void findByUserShouldReturnNull() {
    User testUser = userService.findUserByEmail("test@email8.com");

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
    long initialDatabaseCount = service.count();
    User user = userService.findUserByEmail("test@email4.com");
    VerificationToken token = new VerificationToken(user);
    service.saveToken(token);

    assertEquals(1, service.count() - initialDatabaseCount);
  }

  @Test
  public void deleteTokenShouldSucceed() {
    long initialDatabaseCount = service.count();
    User user = userService.findUserByEmail("test@email2.com");
    VerificationToken token = service.findByUser(user);

    service.deleteVerificationToken(token);
    assertEquals(1, initialDatabaseCount - service.count());
  }

  @Test
  public void deleteTokenShouldFail() {
    long initialDatabaseCount = service.count();
    User user = userService.findUserByEmail("test@email8.com");
    VerificationToken token = new VerificationToken(user);

    service.deleteVerificationToken(token);
    assertEquals(0, initialDatabaseCount - service.count());
  }
}
