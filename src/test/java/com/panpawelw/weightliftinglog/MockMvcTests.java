package com.panpawelw.weightliftinglog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testRegularUser() throws Exception {
    this.mockMvc.perform(get("/WeightliftingLog")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("everybody")));
  }

  @Test
  public void testUserAttemptsAdmin() throws Exception {
    this.mockMvc.perform(get("/WeightliftingLog/admin"))
        .andExpect(status().isUnauthorized());
  }

  @Configuration
  public static class MyTestConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/WeightliftingLog/admin/**")
          .hasRole("USER").and().httpBasic();
    }
  }

  @Controller
  @TestConfiguration
  public static class TestController {

    @RequestMapping(path = "/WeightliftingLog")
    public ResponseEntity<String> everybody() {
      return ResponseEntity.ok("everybody");
    }

    @RequestMapping(path = "/WeightliftingLog/user")
    public ResponseEntity<String> user() {
      return ResponseEntity.ok("user");
    }

    @RequestMapping(path = "/WeightliftingLog/admin")
    public ResponseEntity<String> admin() {
      return ResponseEntity.ok("admin");
    }
  }
}
