package com.panpawelw.weightliftinglog.servicetests;

import com.panpawelw.weightliftinglog.models.SecureUserDetails;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.repositories.UserRepository;
import com.panpawelw.weightliftinglog.services.SecureUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecureUserDetailsServiceTests {

  private static final SecureUserDetails TEST_USER = new SecureUserDetails(
      new User(1L, "Existing user", "Test password",
          "null", "test@email.com", true));

  @Mock
  private UserRepository repository;

  private SecureUserDetailsService service;

  @Before
  public void setup() {
    service = new SecureUserDetailsService(repository);
  }

  @Test
  public void testLoadUserByUsernameWhenUserExists() {
    when(repository.findUserByName("Existing user")).thenReturn(Optional.of(TEST_USER));

    assertEquals(service.loadUserByUsername("Existing user"),
        TEST_USER, "Username should exist!");
    verify(repository).findUserByName("Existing user");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsernameWhenUserDoesntExist() throws UsernameNotFoundException {
    when(repository.findUserByName("Not existing user")).thenReturn(Optional.empty());

    service.loadUserByUsername("Not existing user");
    verify(repository).findUserByName("Not existing user");
  }
}
