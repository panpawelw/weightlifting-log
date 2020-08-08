package com.panpawelw.weightliftinglog.validators;

import com.panpawelw.weightliftinglog.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.panpawelw.weightliftinglog.models.ChangePassword;

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
     * @param o - user object
     * @param errors - validation errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        ChangePassword changePassword = (ChangePassword) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmOldPassword", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmNewPassword", "NotBlank");

        if (changePassword.getOldPassword().length() < 4
                || changePassword.getOldPassword().length() > 32) {
            errors.rejectValue("oldPassword", "Size.userForm.password");
        }if (changePassword.getConfirmOldPassword().length() < 4
                || changePassword.getConfirmNewPassword().length() > 32) {
            errors.rejectValue("confirmOldPassword", "Size.userForm.password");
        }if (changePassword.getNewPassword().length() < 4
                || changePassword.getNewPassword().length() > 32) {
            errors.rejectValue("newPassword", "Size.userForm.password");
        }if (changePassword.getConfirmNewPassword().length() < 4
                || changePassword.getConfirmNewPassword().length() > 32) {
            errors.rejectValue("confirmNewPassword", "Size.userForm.password");
        }

        if (!changePassword.getOldPassword().equals(changePassword.getConfirmOldPassword())) {
            errors.rejectValue("oldPassword", "Diff.userForm.confirmPassword");
            errors.rejectValue("confirmOldPassword", "Diff.userForm.confirmPassword");
        }
        if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword())) {
            errors.rejectValue("newPassword", "Diff.userForm.confirmPassword");
            errors.rejectValue("confirmNewPassword", "Diff.userForm.confirmPassword");
        }

        if(userService.passwordsDontMatch(changePassword.getOldPassword())) {
            errors.rejectValue("oldPassword", "Diff.userForm.wrongPassword");
        }
    }
}
