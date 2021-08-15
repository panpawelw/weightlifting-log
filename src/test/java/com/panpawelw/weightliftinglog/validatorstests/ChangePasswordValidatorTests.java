package com.panpawelw.weightliftinglog.validatorstests;

import com.panpawelw.weightliftinglog.models.ChangePassword;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.validators.ChangePasswordValidator;
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
public class ChangePasswordValidatorTests {

  private static final ChangePassword TEST_CHANGEPASSWORD = new ChangePassword(
      "password", "password",
      "password1", "password1");

  @Mock
  private UserService service;

  private ChangePasswordValidator validator;

  @Before
  public void setup() {
    validator = new ChangePasswordValidator(service);
  }

  @Test
  public void supports() {
    assertTrue(validator.supports(ChangePassword.class));
    assertFalse(validator.supports(Object.class));
  }

  @Test
  public void changePasswordIsValid() {
    BindException errors = new BindException(TEST_CHANGEPASSWORD, "changePassword");
    when(service.passwordsDontMatch(TEST_CHANGEPASSWORD.getOldPassword())).thenReturn(false);
    ValidationUtils.invokeValidator(validator, TEST_CHANGEPASSWORD, errors);
    assertFalse(errors.hasErrors());
  }

}
