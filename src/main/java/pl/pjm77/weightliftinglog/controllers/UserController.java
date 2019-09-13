package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.services.WorkoutService;
import pl.pjm77.weightliftinglog.validators.UpdatePasswordValidator;
import pl.pjm77.weightliftinglog.services.UserService;

import javax.validation.Valid;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;
import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

@Controller
public class UserController {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final UpdatePasswordValidator updatePasswordValidator;

    @Autowired
    public UserController(UserService userService, WorkoutService workoutService,
                          UpdatePasswordValidator updatePasswordValidator) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.updatePasswordValidator = updatePasswordValidator;
    }

    @InitBinder("user")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(updatePasswordValidator);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model) {
        String username = getLoggedInUserName();
        model.addAttribute("userName", username);
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-default");
        User user = userService.findUserByName(username);
        model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
        return "home";
    }

    @GetMapping("/user/update")
    public String editUserDetails(Model model) {
        String userName = getLoggedInUserName();
        User user = userService.findUserByName(userName);
        model.addAttribute("user", user);
        model.addAttribute("page", "fragments.html :: update-user");
        return "home";
    }

    @PostMapping("/user/update")
    public String registerPost(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) {
        model.addAttribute("page", "fragments.html :: update-user");
        if (!bindingResult.hasErrors()) {
            try {
                userService.saveUser(user);
                model.addAttribute("page", "fragments.html :: update-user-success");
            }catch(DataIntegrityViolationException e){
                model.addAttribute
                        ("emailExists", "    This email already exists in our database!");
            }
        }
        return "home";
    }
}