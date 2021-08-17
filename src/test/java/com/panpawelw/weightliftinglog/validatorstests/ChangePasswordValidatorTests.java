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

import java.util.Objects;

import static org.junit.Assert.*;
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

  @Test
  public void allFieldsAreEmpty() {
    ChangePassword changePassword =
        new ChangePassword("","","","");
    BindException errors = new BindException(changePassword, "changePassword");
    when(service.passwordsDontMatch(changePassword.getOldPassword())).thenReturn(false);
    ValidationUtils.invokeValidator(validator, changePassword, errors);
    assertEquals(errors.getErrorCount(), 5);
    assertEquals("NotBlank.oldPassword",
        Objects.requireNonNull(errors.getFieldErrors("oldPassword")).get(0).getCode());
    assertEquals("Size.password",
        Objects.requireNonNull(errors.getFieldErrors("oldPassword")).get(1).getCode());
    assertEquals("NotBlank.oldConfirmPassword",
        Objects.requireNonNull(errors.getFieldError("oldConfirmPassword")).getCode());
    assertEquals("NotBlank.password",
        Objects.requireNonNull(errors.getFieldError("newPassword")).getCode());
    assertEquals("NotBlank.confirmPassword",
        Objects.requireNonNull(errors.getFieldError("newConfirmPassword")).getCode());
  }
}
