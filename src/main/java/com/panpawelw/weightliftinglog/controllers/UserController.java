package com.panpawelw.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.services.VerificationTokenService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import com.panpawelw.weightliftinglog.validators.UpdatePasswordValidator;
import com.panpawelw.weightliftinglog.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final UpdatePasswordValidator updatePasswordValidator;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public UserController(UserService userService, WorkoutService workoutService,
                          UpdatePasswordValidator updatePasswordValidator,
                          VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.updatePasswordValidator = updatePasswordValidator;
        this.verificationTokenService = verificationTokenService;
    }

    @InitBinder("user")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(updatePasswordValidator);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model, HttpServletRequest request, HttpServletResponse response) {
        String email = userService.getLoggedInUsersEmail();
        User user = userService.findUserByEmail(email);
        if (!user.isActivated()) {
            userService.logoutUser(request, response);
            model.addAttribute("loginError", verificationTokenService.removeAccountIfTokenExpired(user));
            model.addAttribute("page", "fragments.html :: login");
            return "home";
        }
        model.addAttribute("userName", user.getName());
        model.addAttribute("adminRights", userService.checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-default");
        model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
        return "home";
    }

    @GetMapping("/user/update")
    public String editUserDetailsGet(Model model) {
        String email = userService.getLoggedInUsersEmail();
        User user = userService.findUserByEmail(email);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("page", "fragments.html :: update-user");
        return "home";
    }

    @PostMapping("/user/update")
    public String editUserDetailsPost(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model,
                               HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("page", "fragments.html :: update-user");
        if (!bindingResult.hasErrors()) {
            try {
                String password = user.getPassword();
                userService.saveUser(user);
                userService.logoutUser(request, response);
                userService.autoLogin(request, user.getName(), password);
                model.addAttribute("page", "fragments.html :: update-user-success");
            }catch(DataIntegrityViolationException e){
                model.addAttribute
                        ("emailExists", "    This email already exists in our database!");
            }
        }
        return "home";
    }
}