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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
  public void saveUserWithoutModifyingPasswordShouldSucceed() {
    final String CHANGED_NAME = "Changed name";
    Long databaseCount = service.count();
    User user = service.findUserByEmail("test@email3.com");
    final String initialPassword = user.getPassword();
    user.setName(CHANGED_NAME);
    service.saveUserWithoutModifyingPassword(user);
    assertEquals(initialPassword, service.findUserByEmail("test@email3.com").getPassword());
    assertEquals(CHANGED_NAME, service.findUserByEmail("test@email3.com").getName());
    assertEquals(databaseCount, service.count());
  }

  @Test
  public void deleteUserByIdShouldSucceed() {
    Long count = service.count();
    service.deleteUserById(4);
    assertEquals(1, count - service.count());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteUserByIdShouldFail() {
    service.deleteUserById(100);
  }

  @Test
  public void findAllUsersByActivatedShouldSucceed() {
    long count = service.count();
    List<User> activatedUsers = service.findAllByActivated(true);
    List<User> notActivatedUsers = service.findAllByActivated(false);
    long countActivatedUsers = activatedUsers.stream().filter(User::isActivated).count();
    long countNotActivatedUsers = notActivatedUsers.stream().filter(c -> !c.isActivated()).count();
    assertEquals(activatedUsers.size(), countActivatedUsers);
    assertEquals(notActivatedUsers.size(), countNotActivatedUsers);
    assertEquals(count, activatedUsers.size() + notActivatedUsers.size());
  }

  @Test
  @WithUserDetails("Test name5")
  public void changeCurrentUserPasswordShouldSucceed() {
    service.changeCurrentUserPassword("New test password");
    assertTrue(BCrypt.checkpw("New test password",
        service.findUserByEmail("test@email5.com").getPassword()));
  }

  @Test
  @WithMockUser(username = "user")
  public void getLoggedInUserNameShouldSucceed() {
    assertEquals("user", service.getLoggedInUserName());
  }

  @Test(expected = NullPointerException.class)
  public void getLoggedInUserNameShouldThrowException() {
    service.getLoggedInUserName();
  }

  @Test
  @WithUserDetails("Test name5")
  public void getLoggedInUsersEmailShouldSucceed() {
    assertEquals("test@email5.com", service.getLoggedInUsersEmail());
  }

  @Test(expected = NullPointerException.class)
  public void getLoggedInUsersEmailShouldThrowException() {
    service.getLoggedInUsersEmail();
  }

}
