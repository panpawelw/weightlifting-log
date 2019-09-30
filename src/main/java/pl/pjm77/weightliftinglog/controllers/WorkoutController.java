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

    @GetMapping("/workout/details")
    public String addWorkoutGet(Model model) {
        String username =  getLoggedInUserName();
        model.addAttribute("userName", username);
        model.addAttribute("user", userService.findUserByName(UserService.getLoggedInUserName()));
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-workout-details");
        User user = userService.findUserByName(username);
        model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
        return "home";
    }
}