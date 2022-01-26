package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.services.SecureUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecureUserDetailsServiceIT {

  @Autowired
  private SecureUserDetailsService service;

  @Test
  public void LoadUserByUsernameReturnsUser() {
    assertEquals("test", service.loadUserByUsername("test").getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void LoadUserByUsernameThrowsException() {
    service.loadUserByUsername("Not existing name");
  }
}
