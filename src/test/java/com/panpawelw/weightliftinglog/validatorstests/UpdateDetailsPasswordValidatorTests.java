package com.panpawelw.weightliftinglog.validatorstests;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateDetailsPasswordValidatorTests {

  private static final User TEST_USER = new User(1L, "Test username",
      "Test password", "Test password", "test@email.com", true);

  @Mock
  private UserService service;

  private UpdateDetailsPasswordValidator validator;

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
  public void userIsValid() {
    BindException errors = new BindException(TEST_USER, "user");
    when(service.passwordsDontMatch(TEST_USER.getPassword())).thenReturn(false);
    ValidationUtils.invokeValidator(validator, TEST_USER, errors);
    assertFalse(errors.hasErrors());
  }
}
