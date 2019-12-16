package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pjm77.weightliftinglog.models.ChangePassword;
import pl.pjm77.weightliftinglog.services.UserService;
import pl.pjm77.weightliftinglog.validators.ChangePasswordValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class ChangePasswordController {

    private final ChangePasswordValidator changePasswordValidator;
    private final UserService userService;

    @Autowired
    public ChangePasswordController(ChangePasswordValidator changePasswordValidator,
                                    UserService userService) {
        this.changePasswordValidator = changePasswordValidator;
        this.userService = userService;
    }

    @InitBinder("changePassword")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(changePasswordValidator);
    }

    @GetMapping("/user/changepassword")
    public String changePasswordGet(Model model) {
        ChangePassword changePassword = new ChangePassword();
        model.addAttribute("changePassword", changePassword);
        model.addAttribute("page", "fragments.html :: change-password");
        return "home";
    }

    @PostMapping("/user/changepassword")
    public String changePasswordPost(
            @Valid @ModelAttribute("changePassword") ChangePassword changePassword,
            BindingResult bindingResult, Model model, HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("page", "fragments.html :: change-password");
        } else {
            String username =
                    userService.findUserByEmail(UserService.getLoggedInUsersEmail()).getName();
            userService.changeCurrentUserPassword(changePassword.getNewPassword());
            userService.logoutUser(request, response);
            userService.autoLogin(request, username, changePassword.getNewPassword());
            model.addAttribute("page", "fragments.html :: change-password-success");
        }
        return "home";
    }
}