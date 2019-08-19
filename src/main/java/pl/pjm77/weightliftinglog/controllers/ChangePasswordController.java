package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.pjm77.weightliftinglog.models.ChangePassword;
import pl.pjm77.weightliftinglog.services.ChangePasswordService;
import pl.pjm77.weightliftinglog.services.UserService;

@Controller
public class ChangePasswordController {

    private ChangePasswordService changePasswordService;
    private UserService userService;

    @Autowired
    public ChangePasswordController(ChangePasswordService changePasswordService,
                                    UserService userService) {
        this.changePasswordService = changePasswordService;
        this.userService = userService;
    }

    @GetMapping("/changepassword")
    public String changePasswordGet(Model model) {
        model.addAttribute("changePassword", new ChangePassword());
        model.addAttribute("page", "fragments.html :: change-password");
        return "home";
    }

    @PostMapping("/changepassword")
    public String changePasswordPost(Model model) {
        model.addAttribute("page", "fragments.html :: change-password-success");
        return "home";
    }
}