package pl.pjm77.weightliftinglog.security.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.services.UserService;

@Component
public class UpdatePasswordValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UpdatePasswordValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank");
        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        if(!userService.checkCurrentUserPassword(user.getPassword())) {
            errors.rejectValue("password", "Diff.userForm.wrongPassword");
        }
    }
}