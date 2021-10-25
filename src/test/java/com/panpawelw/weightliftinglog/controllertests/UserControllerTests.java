package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.UserController;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import com.panpawelw.weightliftinglog.validators.UpdateDetailsPasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static org.mockito.Mockito.when;

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

  @Test
  public void shouldReturnUserPanel() throws Exception {
    when(service.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(service.findUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER);
    when(service.checkLoggedInUserForAdminRights()).thenReturn(true);

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("userName", TEST_USER.getName()))
        .andExpect(model().attribute("adminRights", true))
        .andExpect(model().attribute("page", "fragments.html :: user-panel"))
        .andExpect(model().attribute("userPanelPage", "fragments.html :: user-panel-default"))
        .andExpect(model().attribute("workouts", TEST_USER.getWorkouts()));
    verify(service).findUserByEmail(TEST_USER.getEmail());
    verify(service).checkLoggedInUserForAdminRights();
  }
}
