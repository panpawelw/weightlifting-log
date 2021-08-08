package com.panpawelw.weightliftinglog.validators;

import com.panpawelw.weightliftinglog.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.panpawelw.weightliftinglog.models.ChangePassword;

import static com.panpawelw.weightliftinglog.misc.Password.passwordIsOK;

/**
 * Used when user is changing his existing password.
 */
@Component
public class ChangePasswordValidator implements Validator {

  private final UserService userService;

  public ChangePasswordValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return ChangePassword.class.equals(aClass);
  }

  /**
   * Checks if any of the fields are empty and if all have required length. Also checks if old
   * password matches old password confirmation field, if old password matches existing
   * password and if new password matches new password confirmation field.
   *
   * @param o      - user object
   * @param errors - validation errors
   */
  @Override
  public void validate(Object o, Errors errors) {
    ChangePassword changePassword = (ChangePassword) o;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors,
        "oldPassword", "NotBlank.oldPassword");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors,
        "confirmOldPassword", "NotBlank.oldConfirmPassword");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword",
        "NotBlank.password");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmNewPassword",
        "NotBlank.confirmPassword");

    if (!passwordIsOK(changePassword.getOldPassword())) {
      errors.rejectValue("oldPassword", "Size.password");
    }
    if (!passwordIsOK(changePassword.getConfirmOldPassword())) {
      errors.rejectValue("confirmOldPassword", "Size.password");
    }
    if (!passwordIsOK(changePassword.getNewPassword())) {
      errors.rejectValue("newPassword", "Size.password");
    }
    if (!passwordIsOK(changePassword.getConfirmNewPassword())) {
      errors.rejectValue("confirmNewPassword", "Size.password");
    }

    if (!changePassword.getOldPassword().equals(changePassword.getConfirmOldPassword())) {
      errors.rejectValue("oldPassword", "Diff.confirmPassword");
      errors.rejectValue("confirmOldPassword", "Diff.confirmPassword");
    }
    if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword())) {
      errors.rejectValue("newPassword", "Diff.confirmPassword");
      errors.rejectValue("confirmNewPassword", "Diff.confirmPassword");
    }

    if (userService.passwordsDontMatch(changePassword.getOldPassword())) {
      errors.rejectValue("oldPassword", "Diff.wrongPassword");
    }
  }
}
