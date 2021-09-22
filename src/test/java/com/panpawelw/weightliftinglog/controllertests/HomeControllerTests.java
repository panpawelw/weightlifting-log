package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.HomeController;
import com.panpawelw.weightliftinglog.models.SecureUserDetails;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.validators.RegistrationPasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = HomeController.class)
@Import(RegistrationPasswordValidator.class)
public class HomeControllerTests {

  private static final User TEST_USER = new User(1L, "Test name",
      "Test password", "Test password",
      "test@email.com", true, "Test first name",
      "Test last name", 20, true, "ADMIN", new ArrayList<>());

  private static final SecureUserDetails TEST_SECUREUSERDETAILS = new SecureUserDetails(TEST_USER);

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @Mock
  private ApplicationEventPublisher publisher;

  @Mock
  private VerificationTokenService verificationTokenService;

  @Before
  public void setup() {
    RegistrationPasswordValidator validator = new RegistrationPasswordValidator();
    HomeController controller = new HomeController(userService, validator, publisher,
        verificationTokenService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void homeControllerGet() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("showCalc", true))
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: login"));
  }

  @Test
  public void validLogin() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(TEST_SECUREUSERDETAILS, TEST_USER.getPassword()));
    mockMvc.perform(get("/login"))
        .andExpect(status().is(302))
        .andExpect(redirectedUrl("/user"));
  }

  @Test
  public void invalidLogin() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new Object(), new Object(), Collections.emptyList()));
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: login"));
  }

  @Test
  public void loginPost() throws Exception {
    mockMvc.perform(post("/login"))
        .andExpect(status().is(302))
        .andExpect(redirectedUrl("/user"));
  }

  @Test
  public void loginFailure() throws Exception {
    mockMvc.perform(get("/loginfailure"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: login"));
  }

  @Test
  public void validLogout() throws Exception {
    mockMvc.perform(get("/logout"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("header", "Logout successful!"))
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: show-message"));
    verify(userService).logoutUser(any(), any());
  }

  @Test
  public void invalidLogout() throws Exception {
    Authentication authentication = new TestingAuthenticationToken(new Object(), new Object(), Collections.emptyList());
    when(userService.logoutUser(any(), any())).thenReturn(authentication);
    mockMvc.perform(get("/logout"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("header", "Logout failure!"))
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: show-message"));
    verify(userService).logoutUser(any(), any());
  }

  @Test
  public void registerGet() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: register-user"))
        .andExpect(model().attribute("user", org.hamcrest.Matchers.any(User.class)));
  }

  @Test
  public void validRegistrationUserActivationNotNeeded() throws Exception {
    mockMvc.perform(post("/register").flashAttr("user", TEST_USER))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("text",
            "Your user account has been registered and activated. Enjoy!"));
  }

  @Test
  public void validRegistrationUserActivationNeeded() throws Exception {
    User testUser = new User(TEST_USER);
    testUser.setActivated(false);
    testUser.setConfirmPassword("Test password");
    mockMvc.perform(post("/register").flashAttr("user", testUser))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("text",
            "Confirmation email has been sent to:<br><br>test@email.com<br><br>Please activate" +
                " your account within 24 hours!<br><br>Don't forget to check your spam folder!"));
    verify(publisher).publishEvent(any());
  }

  @Test
  public void usersNameExists() throws Exception {
    when(userService.saveUser(TEST_USER))
        .thenThrow(new DataIntegrityViolationException("user_unique_name_idx"));
    mockMvc.perform(post("/register").flashAttr("user", TEST_USER))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attributeHasFieldErrors("user", "name"));

    verify(userService).saveUser(TEST_USER);
  }

  @Test
  public void usersEmailExists() throws Exception {
    when(userService.saveUser(TEST_USER))
        .thenThrow(new DataIntegrityViolationException("user_unique_email_idx"));
    mockMvc.perform(post("/register").flashAttr("user", TEST_USER))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attributeHasFieldErrors("user","email"));
    verify(userService).saveUser(TEST_USER);
  }
}
