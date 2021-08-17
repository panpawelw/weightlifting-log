package com.panpawelw.weightliftinglog.validators;

import com.panpawelw.weightliftinglog.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.panpawelw.weightliftinglog.models.ChangePassword;

import static com.panpawelw.weightliftinglog.validators.misc.Password.passwordIsOK;

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
        "oldConfirmPassword", "NotBlank.oldConfirmPassword");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword",
        "NotBlank.password");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newConfirmPassword",
        "NotBlank.confirmPassword");

    if (!changePassword.getOldPassword().equals(changePassword.getOldConfirmPassword())) {
      errors.rejectValue("oldPassword", "Diff.confirmPassword");
      errors.rejectValue("oldConfirmPassword", "Diff.confirmPassword");
    }
    if (!changePassword.getNewPassword().equals(changePassword.getNewConfirmPassword())) {
      errors.rejectValue("newPassword", "Diff.confirmPassword");
      errors.rejectValue("newConfirmPassword", "Diff.confirmPassword");
    }

    if (passwordIsOK(changePassword.getOldPassword()) &&
        userService.passwordsDontMatch(changePassword.getOldPassword())) {
      errors.rejectValue("oldPassword", "Diff.wrongPassword");
    }

    if (!passwordIsOK(changePassword.getOldPassword()) ||
        !passwordIsOK(changePassword.getOldConfirmPassword()) ||
        !passwordIsOK(changePassword.getNewPassword()) ||
        !passwordIsOK(changePassword.getNewConfirmPassword())) {
      errors.rejectValue("oldPassword", "Size.password");
    }
  }
}
