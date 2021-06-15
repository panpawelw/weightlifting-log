package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.repositories.UserRepository;
import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

  private static final User TEST_USER = new User(1L, "Test name",
      "Test password", "Test password",
      "Test@email.com", true, "Test first name",
      "Test last name", 20, true, "USER", new ArrayList<>());

  @Mock
  private UserRepository repository;

  @Mock
  private PasswordEncoder encoder;

  @Mock
  private AuthenticationManager manager;

  private UserService service;

  @Before
  public void setup() {
    this.service = new UserService(repository, encoder, manager);
  }

  @Test
  public void testFindUserByEmail() {
    when(repository.findUserByEmail(TEST_USER.getEmail()))
        .thenReturn(java.util.Optional.of(TEST_USER));
    assertEquals(TEST_USER, service.findUserByEmail(TEST_USER.getEmail()));
  }

  @Test
  public void testFindUserByEmailReturnsNull() {
    when(repository.findUserByEmail(TEST_USER.getEmail()))
        .thenReturn(Optional.empty());
    assertNull(service.findUserByEmail(TEST_USER.getEmail()));
  }

  @Test
  public void testSaveUser() {
    when(encoder.encode(TEST_USER.getPassword())).thenReturn(TEST_USER.getPassword());
    when(repository.saveAndFlush(TEST_USER)).thenReturn(TEST_USER);
    service.saveUser(TEST_USER);
    verify(repository).saveAndFlush(TEST_USER);
  }

  @Test
  public void testDeleteUser() {
    service.deleteUserById(1);
    verify(repository).deleteById(1L);
  }
}
