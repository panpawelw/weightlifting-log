package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.UserController;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import com.panpawelw.weightliftinglog.validators.UpdateDetailsPasswordValidator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService service;

  @Mock
  private WorkoutService workoutService;

  @Mock
  private UpdateDetailsPasswordValidator validator;

  @Mock
  private VerificationTokenService verificationTokenService;

  @Before
  public void setup() {
    UserController controller = new UserController(
        service, workoutService, validator, verificationTokenService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }
}
