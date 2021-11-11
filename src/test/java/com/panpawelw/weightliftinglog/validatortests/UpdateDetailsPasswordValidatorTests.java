package com.panpawelw.weightliftinglog.validatortests;

import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.validators.UpdateDetailsPasswordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateDetailsPasswordValidatorTests {

  @Mock
  private UserService service;

  private UpdateDetailsPasswordValidator validator;

  private final BindException errors = new BindException(TEST_USER, "user");

  @Before
  public void setup() {
    validator = new UpdateDetailsPasswordValidator(service);
  }

  @Test
  public void supports() {
    assertTrue(validator.supports(User.class));
    assertFalse(validator.supports(Object.class));
  }

  @Test
  public void passwordIsValid() {
    TEST_USER.setPassword("Test password");
    when(service.passwordsDontMatch(TEST_USER.getPassword())).thenReturn(false);
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void passwordIsBlank() {
    TEST_USER.setPassword("");
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("NotBlank.password", errors.getFieldErrors("password").get(0).getCode());
  }

  @Test
  public void passwordHasWrongLength() {
    User testUser = new User(TEST_USER);
    testUser.setPassword("xx");
    ValidationUtils.invokeValidator(validator, testUser, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("Size.password", errors.getFieldErrors("password").get(0).getCode());
  }

  @Test
  public void wrongPassword() {
    TEST_USER.setPassword("Wrong password");
    when(service.passwordsDontMatch("Wrong password")).thenReturn(true);
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("Diff.wrongPassword", errors.getFieldErrors("password").get(0).getCode());
  }
}
