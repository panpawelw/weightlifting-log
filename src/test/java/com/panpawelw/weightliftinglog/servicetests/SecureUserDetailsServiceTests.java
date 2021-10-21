package com.panpawelw.weightliftinglog.servicetests;

import com.panpawelw.weightliftinglog.models.SecureUserDetails;
import com.panpawelw.weightliftinglog.repositories.UserRepository;
import com.panpawelw.weightliftinglog.services.SecureUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecureUserDetailsServiceTests {

  private static final SecureUserDetails SECURE_TESTUSER = new SecureUserDetails(TEST_USER);

  @Mock
  private UserRepository repository;

  private SecureUserDetailsService service;

  @Before
  public void setup() {
    service = new SecureUserDetailsService(repository);
  }

  @Test
  public void testLoadUserByUsernameWhenUserExists() {
    when(repository.findUserByName("Existing user")).thenReturn(Optional.of(SECURE_TESTUSER));

    assertEquals(service.loadUserByUsername("Existing user"),
        SECURE_TESTUSER, "Username should exist!");
    verify(repository).findUserByName("Existing user");
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsernameWhenUserDoesntExist() throws UsernameNotFoundException {
    when(repository.findUserByName("Not existing user")).thenReturn(Optional.empty());

    service.loadUserByUsername("Not existing user");
    verify(repository).findUserByName("Not existing user");
  }
}
