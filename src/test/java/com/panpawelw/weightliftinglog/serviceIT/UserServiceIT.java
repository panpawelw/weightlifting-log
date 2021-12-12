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
    assertEquals("user@test1.com", service.findUserByEmail("user@test1.com").getEmail());
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
    User user = service.findUserByEmail("user@test1.com");
    user.setId(null);
    user.setEmail("different@email.com");
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
    User user = service.findUserByEmail("user@test2.com");
    user.setId(null);
    user.setName("Different name");
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
    User user = service.findUserByEmail("user@test3.com");
    final String initialPassword = user.getPassword();
    user.setName(CHANGED_NAME);
    service.saveUserWithoutModifyingPassword(user);
    assertEquals(initialPassword, service.findUserByEmail("user@test3.com").getPassword());
    assertEquals(CHANGED_NAME, service.findUserByEmail("user@test3.com").getName());
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
    long databaseCount = service.count();
    List<User> activatedUsers = service.findAllByActivated(true);
    List<User> notActivatedUsers = service.findAllByActivated(false);
    long countActivatedUsers = activatedUsers.stream().filter(User::isActivated).count();
    long countNotActivatedUsers = notActivatedUsers.stream().filter(c -> !c.isActivated()).count();
    assertEquals(activatedUsers.size(), countActivatedUsers);
    assertEquals(notActivatedUsers.size(), countNotActivatedUsers);
    assertEquals(databaseCount, activatedUsers.size() + notActivatedUsers.size());
  }

  @Test
  @WithUserDetails("User test name 5")
  public void changeCurrentUserPasswordShouldSucceed() {
    service.changeCurrentUserPassword("New test password");
    assertTrue(BCrypt.checkpw("New test password",
        service.findUserByEmail("user@test5.com").getPassword()));
  }

  @Test
  @WithUserDetails("User test name 6")
  public void passwordsDontMatchShouldReturnFalse() {
    assertFalse(service.passwordsDontMatch("Test password6"));
  }

  @Test
  @WithUserDetails("User test name 6")
  public void passwordsDontMatchShouldReturnTrue() {
    assertTrue(service.passwordsDontMatch("Wrong password"));
  }

  @Test(expected = NullPointerException.class)
  public void passwordsDontMatchShouldThrowException() {
    service.passwordsDontMatch("password");
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
  @WithUserDetails("User test name 7")
  public void getLoggedInUsersEmailShouldSucceed() {
    assertEquals("user@test7.com", service.getLoggedInUsersEmail());
  }

  @Test(expected = NullPointerException.class)
  public void getLoggedInUsersEmailShouldThrowException() {
    service.getLoggedInUsersEmail();
  }

  @Test
  @WithMockUser(username="admin",roles={"ADMIN"})
  public void checkLoggedInUserForAdminRightsShouldReturnTrue() {
    service.checkLoggedInUserForAdminRights();
  }

  @Test
  @WithMockUser(username="user")
  public void checkLoggedInUserForAdminRightsShouldReturnFalse() {
    service.checkLoggedInUserForAdminRights();
  }

  @Test(expected = NullPointerException.class)
  public void checkLoggedInUserForAdminRightsShouldThrowException() {
    service.checkLoggedInUserForAdminRights();
  }

  @Test
  @WithUserDetails("User test name 8")
  public void getLoggedInUserPasswordShouldSucceed() {
    assertTrue(BCrypt.checkpw("Test password8", service.getLoggedInUserPassword()));
  }

  @Test(expected = NullPointerException.class)
  public void getLoggedInUserPasswordThrowException() {
    service.getLoggedInUserPassword();
  }
}
