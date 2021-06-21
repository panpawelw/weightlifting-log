package com.panpawelw.weightliftinglog.weblayertests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebMockTest.TestController.class)
@RunWith(SpringRunner.class)
public class WebMockTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGreeting() throws Exception {
    this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("Hello!")));
  }

  @Configuration
  public static class MyTestConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/weightliftinglog/admin/**")
          .hasRole("USER").and().httpBasic();
    }
  }

  @Controller
  @TestConfiguration
  public static class TestController {

    private final TestService service;

    public TestController(TestService service) {
      this.service = service;
    }

    @GetMapping(path = "/greeting")
    public ResponseEntity<String> greeting() {
      return ResponseEntity.ok(service.greet());
    }
  }

  @Service
  @TestConfiguration
  public static class TestService {

    public String greet() {
      return "Hello!";
    }
  }
}
