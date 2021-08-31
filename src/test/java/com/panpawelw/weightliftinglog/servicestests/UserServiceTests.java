package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.SecureUserDetails;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.repositories.UserRepository;
import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

  private static final User TEST_USER = new User(1L, "Test name",
      "Test password", "Test password",
      "Test@email.com", true, "Test first name",
      "Test last name", 20, true, "ADMIN", new ArrayList<>());

  private static final SecureUserDetails TEST_SECUREUSERDETAILS = new SecureUserDetails(TEST_USER);

  @Mock
  private UserRepository repository;

  @Mock
  private PasswordEncoder encoder;

  @Mock
  private AuthenticationManager manager;

  private UserService service;

  @Before
  public void setup() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(TEST_SECUREUSERDETAILS, TEST_USER.getPassword()));
    this.service = new UserService(repository, encoder, manager);
  }

  @Test
  public void testFindUserByEmail() {
    when(repository.findUserByEmail(TEST_USER.getEmail()))
        .thenReturn(java.util.Optional.of(TEST_USER));

    assertEquals(TEST_USER, service.findUserByEmail(TEST_USER.getEmail()));
    verify(repository).findUserByEmail(TEST_USER.getEmail());
  }

  @Test
  public void testFindUserByEmailReturnsNull() {
    when(repository.findUserByEmail(TEST_USER.getEmail()))
        .thenReturn(Optional.empty());

    assertNull(service.findUserByEmail(TEST_USER.getEmail()));
    verify(repository).findUserByEmail(TEST_USER.getEmail());
  }

  @Test
  public void testSaveUser() {
    when(encoder.encode(TEST_USER.getPassword())).thenReturn(TEST_USER.getPassword());
    when(repository.saveAndFlush(TEST_USER)).thenReturn(TEST_USER);

    service.saveUser(TEST_USER);
    verify(repository).saveAndFlush(TEST_USER);
  }

  @Test
  public void testSaveUserWithoutModifyingPassword() {
    when(repository.saveAndFlush(TEST_USER)).thenReturn(TEST_USER);

    service.saveUserWithoutModifyingPassword(TEST_USER);
    verify(repository).saveAndFlush(TEST_USER);
  }

  @Test
  public void testDeleteUserById() {
    when(repository.deleteById(1)).thenReturn(1L);
    service.deleteUserById(1);

    verify(repository).deleteById(1L);
  }

  @Test
  public void testFindAllByActivated() {
    List<User> testList = Arrays.asList(new User(), new User(), new User());
    when(repository.findAllByActivated(true)).thenReturn(testList);
    assertEquals(service.findAllByActivated(true), testList);
    verify(repository).findAllByActivated(true);
  }

  @Test
  public void testGetLoggedInUsersName() {
    assertEquals(TEST_USER.getName(), service.getLoggedInUserName());
  }

  @Test
  public void testGetLoggedInUsersEmail() {
    assertEquals(TEST_USER.getEmail(), service.getLoggedInUsersEmail());
  }

  @Test
  public void testcheckLoggedInUserForAdminRights() {
    assertEquals(TEST_USER.getRole().equals("ADMIN"), service.checkLoggedInUserForAdminRights());
  }

  @Test
  public void testGetLoggedInUsersPassword() {
    assertEquals(TEST_USER.getPassword(), service.getLoggedInUserPassword());
  }
}
