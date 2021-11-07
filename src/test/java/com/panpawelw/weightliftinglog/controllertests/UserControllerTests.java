package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.UserController;
import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import com.panpawelw.weightliftinglog.validators.UpdateDetailsPasswordValidator;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.panpawelw.weightliftinglog.constants.TEST_USER;

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
  public void getUserShouldReturnUserPanel() throws Exception {
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

  @Test
  public void getUserShouldReturnCantRetrieveUser() throws Exception {
    when(service.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(service.findUserByEmail(TEST_USER.getEmail())).thenReturn(null);

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("page", "fragments.html :: show-message"))
        .andExpect(model().attribute("header", "Error!"))
        .andExpect(model().attribute("text", "Can't retrieve user!"))
        .andExpect(model().attribute("address", "/weightliftinglog"));
    verify(service).getLoggedInUsersEmail();
    verify(service).findUserByEmail(TEST_USER.getEmail());
  }

  @Test
  public void getUserShouldReturnUserNotActivated() throws Exception {
    User testUser = new User(TEST_USER);
    testUser.setActivated(false);
    when(service.getLoggedInUsersEmail()).thenReturn(testUser.getEmail());
    when(service.findUserByEmail(testUser.getEmail())).thenReturn(testUser);
    when(verificationTokenService.removeAccountIfTokenExpired(testUser))
        .thenReturn("This account requires activation!");

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("loginError", "This account requires activation!"))
        .andExpect(model().attribute("page", "fragments.html :: login"));
    verify(service).getLoggedInUsersEmail();
    verify(service).findUserByEmail(testUser.getEmail());
    verify(verificationTokenService).removeAccountIfTokenExpired(testUser);
  }

  @Test(expected = ApiRequestException.class)
  public void getUserShouldThrowApiRequestException() throws Throwable {
    when(service.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(service.getLoggedInUsersEmail()).thenThrow(HibernateException.class);
    try {
      mockMvc.perform(get("/user"))
          .andExpect(status().isOk());
      verify(service).getLoggedInUserName();
      verify(service).findUserByEmail(TEST_USER.getEmail());
    } catch (NestedServletException e) {
      assertEquals("There's a problem with database connection!", e.getCause().getMessage());
      throw e.getCause();
    }
  }

  @Test
  public void updateUserDetailsGetShouldReturnUserDetails() throws Exception {
    User testUser = new User(TEST_USER);
    when(validator.supports(User.class)).thenReturn(true);
    when(service.getLoggedInUsersEmail()).thenReturn(testUser.getEmail());
    when(service.findUserByEmail(testUser.getEmail())).thenReturn(testUser);

    mockMvc.perform(get("/user/update"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("user", testUser))
        .andExpect(model().attribute("page", "fragments.html :: update-user"));
    verify(validator).supports(User.class);
    verify(service).getLoggedInUsersEmail();
    verify(service).findUserByEmail(testUser.getEmail());
  }

  @Test
  public void updateUserDetailsPostShouldReturnSuccess() throws Exception {
    Authentication authentication =
        new TestingAuthenticationToken(new Object(), new Object(), Collections.emptyList());
    when(validator.supports(User.class)).thenReturn(true);
    when(service.saveUserWithoutModifyingPassword(TEST_USER)).thenReturn(TEST_USER.getId());
    when(service.logoutUser(any(), any())).thenReturn(authentication);
    doNothing().when(service).autoLogin(any(), any(), any());
    mockMvc.perform(post("/user/update").flashAttr("user", TEST_USER))
        .andExpect(model().attribute("page", "fragments.html :: show-message"))
        .andExpect(model().attribute("header", "Success!"))
        .andExpect(model().attribute("text", "User details have been updated!"))
        .andExpect(status().isOk());
    verify(validator).supports(User.class);
    verify(service).saveUserWithoutModifyingPassword(TEST_USER);
    verify(service).logoutUser(any(), any());
    verify(service).autoLogin(any(), any(), any());
  }

  @Test
  public void updateUserDetailsPostShouldReturnValidationError() throws Exception {
    User wrongUser = new User();
    when(validator.supports(User.class)).thenReturn(true);
    mockMvc.perform(post("/user/update").flashAttr("user", wrongUser))
        .andExpect(model().attribute("page", "fragments.html :: update-user"))
        .andExpect(forwardedUrl("home"))
        .andExpect(status().isOk());
    verify(validator).supports(User.class);
  }

  @Test
  public void updateUserDetailsPostShouldReturnUserNameExists() throws Exception {
    when(validator.supports(User.class)).thenReturn(true);
    when(service.saveUserWithoutModifyingPassword(TEST_USER))
        .thenThrow(new DataIntegrityViolationException("user_unique_name_idx"));
    mockMvc.perform(post("/user/update").flashAttr("user", TEST_USER))
        .andExpect(model().attributeHasFieldErrors("user", "name"))
        .andExpect(status().isOk());
    verify(validator).supports(User.class);
    verify(service).saveUserWithoutModifyingPassword(TEST_USER);
  }

  @Test
  public void updateUserDetailsPostShouldReturnEmailExists() throws Exception {
    when(validator.supports(User.class)).thenReturn(true);
    when(service.saveUserWithoutModifyingPassword(TEST_USER))
        .thenThrow(new DataIntegrityViolationException("user_unique_email_idx"));
    mockMvc.perform(post("/user/update").flashAttr("user", TEST_USER))
        .andExpect(model().attributeHasFieldErrors("user", "email"))
        .andExpect(status().isOk());
    verify(validator).supports(User.class);
    verify(service).saveUserWithoutModifyingPassword(TEST_USER);
  }
}
