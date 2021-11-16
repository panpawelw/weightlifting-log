package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT {

  @Autowired
  private UserService service;

  @Before
  public void setup() {
    service.saveUser(TEST_USER);
  }

  @Test
  public void findUserByEmailShouldReturnUser() {
    assertEquals(TEST_USER, service.findUserByEmail(TEST_USER.getEmail()));
  }

  @Test
  public void findUserByEmailShouldReturnNull() {
    assertNull(service.findUserByEmail("madeup@email.lol"));
  }

  @Test
  public void deleteUserByIdShouldSucceed() {
    Long count = service.count();
    service.deleteUserById(1);
    assertEquals(count - service.count(), 1);
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteUserByIdShouldFail() {
    service.deleteUserById(100);
  }
}
