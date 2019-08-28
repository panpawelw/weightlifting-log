package pl.pjm77.weightliftinglog.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;
import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {this.workoutService = workoutService;}

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/add")
    public String addWorkoutGet(Model model) {
        model.addAttribute("userGreeting", "Hello " + getLoggedInUserName() + "!");
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-add-workout");
        return "home";
    }
}