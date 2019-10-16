package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.services.UserService;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;
import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @GetMapping("/")
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

    @ResponseBody
    @GetMapping("/{workoutId}")
    public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
        return workoutService.findWorkoutById(workoutId);
    }

    @ResponseBody
    @PostMapping("/")
    public void addWorkoutPost(@RequestBody WorkoutDeserialized workoutDeserialized) {
        workoutDeserialized.setUser
                (userService.findUserByName(UserService.getLoggedInUserName()));
        workoutService.saveWorkout(workoutDeserialized);
    }

    @ResponseBody
    @DeleteMapping("/{workoutId}")
    public void deleteWorkout(@PathVariable long workoutId) {
        workoutService.deleteWorkout(workoutId);
    }
}