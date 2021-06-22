package com.panpawelw.weightliftinglog.securitytests;

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
import org.springframework.security.test.context.support.WithMockUser;
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
public class SecurityTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(username="anyone", roles={})
  public void testAnyoneAttemptsUnsecured() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("everybody")));
  }

  @Test
  @WithMockUser(username="anyone", roles={})
  public void testAnyoneAttemptsUser() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/user")).andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username="anyone", roles={})
  public void testAnyoneAttemptsAdmin() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/admin"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username="user")
  public void testUserAttemptsUnsecured() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("everybody")));
  }

  @Test
  @WithMockUser(username="user")
  public void testUserAttemptsUser() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/user")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("user")));
  }

  @Test
  @WithMockUser(username="user")
  public void testUserAttemptsAdmin() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/admin"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username="admin",roles={"USER", "ADMIN"})
  public void testAdminAttemptsUnsecured() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("everybody")));
  }

  @Test
  @WithMockUser(username="admin",roles={"USER", "ADMIN"})
  public void testAdminAttemptsUser() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/user")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("user")));
  }

  @Test
  @WithMockUser(username="admin",roles={"USER", "ADMIN"})
  public void testAdminAttemptsAdmin() throws Exception {
    this.mockMvc.perform(get("/weightliftinglog/admin")).andDo(print())
        .andExpect(status().isOk()).andExpect(content()
        .string(containsString("admin")));
  }


  @Configuration
  public static class MyTestConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
          .antMatchers("/weightliftinglog/user/**").hasAnyRole("USER", "ADMIN")
          .antMatchers("/weightliftinglog/admin/**").hasRole("ADMIN")
          .and().httpBasic();
    }
  }

  @Controller
  @TestConfiguration
  public static class TestController {

    @RequestMapping(path = "/weightliftinglog")
    public ResponseEntity<String> everybody() {
      return ResponseEntity.ok("everybody");
    }

    @RequestMapping(path = "/weightliftinglog/user")
    public ResponseEntity<String> user() {
      return ResponseEntity.ok("user");
    }

    @RequestMapping(path = "/weightliftinglog/admin")
    public ResponseEntity<String> admin() {
      return ResponseEntity.ok("admin");
    }
  }
}
