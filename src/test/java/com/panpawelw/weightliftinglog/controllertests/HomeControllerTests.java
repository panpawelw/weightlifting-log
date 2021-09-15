package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.HomeController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = HomeController.class)
@Import(RegistrationPasswordValidator.class)
public class HomeControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @Mock
  ApplicationEventPublisher publisher;

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
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  public void loginFails() throws Exception {
    mockMvc.perform(get("/loginfailure"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: login"));
  }

}
