package com.panpawelw.weightliftinglog.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.panpawelw.weightliftinglog.models.User;

/**
 * Used during registration process.
 */
@Component
public class RegistrationPasswordValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return User.class.equals(aClass);
  }

  /**
   * Checks if password is not empty, has proper size and matches password confirmation.
   *
   * @param o      - user object
   * @param errors - validation errors
   */
  @Override
  public void validate(Object o, Errors errors) {
    User user = (User) o;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank.password");
    ValidationUtils.rejectIfEmptyOrWhitespace
        (errors, "confirmPassword", "NotBlank.confirmPassword");
    if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
      errors.rejectValue("password", "Size.userForm.password");
    }

    if (!user.getConfirmPassword().equals(user.getPassword())) {
      errors.rejectValue("confirmPassword", "Diff.userForm.confirmPassword");
    }
  }
}