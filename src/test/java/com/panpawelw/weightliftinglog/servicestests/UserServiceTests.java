package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.repositories.UserRepository;
import com.panpawelw.weightliftinglog.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
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
  }

  @Test
  public void testAutoLogin() {
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getSession()).thenReturn(new MockHttpSession());
    service.autoLogin(mockRequest, TEST_USER.getName(), TEST_USER.getPassword());
    verify(manager).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  public void testChangeCurrentUsersPassword() {
    UserService spyService = spy(service);
    doReturn(TEST_USER.getEmail()).when(spyService).getLoggedInUsersEmail();
    when(repository.findUserByEmail(anyString())).thenReturn(Optional.of(TEST_USER));
    when(encoder.encode("Another test password")).thenReturn("Another test password");
    when(repository.saveAndFlush(TEST_USER)).thenReturn(TEST_USER);

    spyService.changeCurrentUserPassword("Another test password");
    assertEquals(TEST_USER.getPassword(), "Another test password");
  }

  @Test
  public void testPasswordsDontMatch() {
    UserService spyService = spy(service);
    doReturn(TEST_USER.getPassword()).when(spyService).getLoggedInUserPassword();
    when(encoder.matches(any(), any())).thenReturn(true);
    assertFalse(spyService.passwordsDontMatch(TEST_USER.getPassword()));
  }
}
