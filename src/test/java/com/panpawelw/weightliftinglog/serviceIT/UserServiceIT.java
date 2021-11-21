package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT {

  @Autowired
  private UserService service;

  @Test
  public void findUserByEmailShouldReturnUser() {
    assertEquals("test@email1.com", service.findUserByEmail("test@email1.com").getEmail());
  }

  @Test
  public void findUserByEmailShouldReturnNull() {
    assertNull(service.findUserByEmail("madeup@email.lol"));
  }

  @Test
  public void saveUserShouldSucceed() {
    Long databaseCount = service.count();
    User user = new User(TEST_USER);
    user.setId(null);
    assertNotNull(service.saveUser(user));
    assertEquals(1, service.count() - databaseCount);
    assertTrue(BCrypt.checkpw(TEST_USER.getPassword(),
        service.findUserByEmail(TEST_USER.getEmail()).getPassword()));
  }

  @Test
  public void saveUserShouldThrowExceptionDuplicateName() {
    Long databaseCount = service.count();
    User user = service.findUserByEmail("test@email1.com");
    user.setId(null);
    user.setEmail("test@email99.com");
    try {
      service.saveUser(user);
    } catch (DataIntegrityViolationException e) {
      assertTrue(e.getMostSpecificCause().getMessage().contains("PUBLIC.USER_UNIQUE_NAME"));
    } finally {
      assertEquals(databaseCount, service.count());
    }
  }

  @Test
  public void saveUserShouldThrowExceptionDuplicateEmail() {
    Long databaseCount = service.count();
    User user = service.findUserByEmail("test@email2.com");
    user.setId(null);
    user.setName("Test name99");
    try {
      service.saveUser(user);
    } catch (DataIntegrityViolationException e) {
      assertTrue(e.getMostSpecificCause().getMessage().contains("PUBLIC.USER_UNIQUE_EMAIL"));
    } finally {
      assertEquals(databaseCount, service.count());
    }
  }

  @Test
  public void deleteUserByIdShouldSucceed() {
    Long count = service.count();
    service.deleteUserById(5);
    assertEquals(1, count - service.count());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteUserByIdShouldFail() {
    service.deleteUserById(100);
  }
}
