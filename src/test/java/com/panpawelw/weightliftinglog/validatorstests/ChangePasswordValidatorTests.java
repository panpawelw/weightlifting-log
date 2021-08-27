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
      "oldpassword", "oldpassword",
      "newpassword", "newpassword");

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
  public void allFieldsAreBlank() {
    ChangePassword blankChangePassword =
        new ChangePassword("","","","");
    BindException errors = new BindException(blankChangePassword, "changePassword");
    ValidationUtils.invokeValidator(validator, blankChangePassword, errors);
    assertEquals(4, errors.getErrorCount());
    assertEquals("NotBlank.oldPassword", errors.getFieldErrors("oldPassword").get(0).getCode());
    assertEquals("NotBlank.oldConfirmPassword",
        Objects.requireNonNull(errors.getFieldError("oldConfirmPassword")).getCode());
    assertEquals("NotBlank.newPassword",
        Objects.requireNonNull(errors.getFieldError("newPassword")).getCode());
    assertEquals("NotBlank.newConfirmPassword",
        Objects.requireNonNull(errors.getFieldError("newConfirmPassword")).getCode());
  }

  @Test
  public void allFieldsAreTooShort() {
    ChangePassword tooShortChangePassword =
        new ChangePassword("x", "x", "x", "x");
    BindException errors = new BindException(tooShortChangePassword, "changePassword");
    ValidationUtils.invokeValidator(validator, tooShortChangePassword, errors);
    assertEquals(2, errors.getErrorCount());
    assertEquals("Size.oldPassword", errors.getFieldErrors("oldPassword").get(0).getCode());
    assertEquals("Size.newPassword", errors.getFieldErrors("newPassword").get(0).getCode());
  }

  @Test
  public void bothOldAndNewPasswordsDontMatchWithConfirmations() {
    ChangePassword notMatchingChangePassword = new ChangePassword("oldpassword",
        "whatever", "newpassword", "whatever");
    BindException errors = new BindException(notMatchingChangePassword, "changePassword");
    ValidationUtils.invokeValidator(validator, notMatchingChangePassword, errors);
    assertEquals(2, errors.getErrorCount());
    assertEquals("Diff.confirmOldPassword",
        errors.getFieldErrors("oldPassword").get(0).getCode());
    assertEquals("Diff.confirmNewPassword",
        errors.getFieldErrors("newPassword").get(0).getCode());
  }

  @Test
  public void oldPasswordIsWrong() {
    ChangePassword oldPasswordWrongChangePassword = new ChangePassword("wrongpassword",
        "wrongpassword", "newpassword", "newpassword");
    BindException errors = new BindException(oldPasswordWrongChangePassword, "changePassword");
    when(service.passwordsDontMatch(oldPasswordWrongChangePassword.getOldPassword())).thenReturn(true);
    ValidationUtils.invokeValidator(validator, oldPasswordWrongChangePassword, errors);
    assertEquals(1, errors.getErrorCount());
    assertEquals("Diff.wrongPassword", errors.getFieldErrors("oldPassword").get(0).getCode());
  }
}
