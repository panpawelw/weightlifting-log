package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.services.UserService;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;
import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

@Controller
public class WorkoutController {

    private final UserService userService;
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(UserService userService, WorkoutService workoutService) {
        this.userService = userService;
        this.workoutService = workoutService;
    }

    @GetMapping("/workout/add")
    public String addWorkoutGet(Model model) {
        String username =  getLoggedInUserName();
        model.addAttribute("userName", username);
        model.addAttribute("user", userService.findUserByName(UserService.getLoggedInUserName()));
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-add-workout");
        User user = userService.findUserByName(username);
        model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
        return "home";
    }

    @GetMapping("/workout/update")
    public String updateWorkoutGet(Model model) {
        model.addAttribute("userName", "Hello " + getLoggedInUserName() + "!");
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-update-workout");
        return "home";
    }
}