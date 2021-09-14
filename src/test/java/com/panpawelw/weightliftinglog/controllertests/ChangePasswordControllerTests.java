package com.panpawelw.weightliftinglog.controllertests;

import com.panpawelw.weightliftinglog.controllers.ChangePasswordController;
import com.panpawelw.weightliftinglog.models.ChangePassword;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.validators.ChangePasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = ChangePasswordController.class)
@Import(ChangePasswordValidator.class)
public class ChangePasswordControllerTests {

  ChangePassword TEST_CHANGEPASSWORD = new ChangePassword("oldpassword",
      "oldpassword", "newpassword", "newpassword");

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService service;

  @Before
  public void setup() {
    ChangePasswordValidator validator = new ChangePasswordValidator(service);
    ChangePasswordController controller = new ChangePasswordController(validator, service);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void testChangePasswordGet() throws Exception {
    mockMvc.perform(get("/user/changepassword"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: change-password"));
  }

  @Test
  public void changePasswordIsValid() throws Exception {
    when(service.getLoggedInUsersEmail()).thenReturn("fake@email.com");
    when(service.findUserByEmail("fake@email.com")).thenReturn(new User());
    doNothing().when(service).changeCurrentUserPassword(TEST_CHANGEPASSWORD.getNewPassword());
    mockMvc.perform(post("/user/changepassword")
            .flashAttr("changePassword", TEST_CHANGEPASSWORD))
        .andExpect(status().isOk())
        .andExpect(model().hasNoErrors())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: show-message"))
        .andExpect(model().attribute("header", "Success!"));
  }

  @Test
  public void databaseException() throws Exception {
    mockMvc.perform(post("/user/changepassword")
            .flashAttr("changePassword", TEST_CHANGEPASSWORD))
        .andExpect(status().isOk())
        .andExpect(model().hasNoErrors())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("page", "fragments.html :: show-message"))
        .andExpect(model().attribute("header", "Error!"));
  }

  @Test
  public void changePasswordIsInvalid() throws Exception {
    ChangePassword invalidChangePassword = new ChangePassword("oldpassword",
        "oldpassword", "wrongpassword", "newpassword");
    mockMvc.perform(post("/user/changepassword")
            .flashAttr("changePassword", invalidChangePassword))
        .andExpect(status().isOk())
        .andExpect(model().hasErrors())
        .andExpect(forwardedUrl("home"));
  }
}
