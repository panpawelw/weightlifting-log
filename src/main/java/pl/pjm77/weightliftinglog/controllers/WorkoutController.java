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
        User user =  userService.findUserByEmail(UserService.getLoggedInUsersEmail());
        model.addAttribute("user", user.getEmail());
        model.addAttribute("userName", user.getName());
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
        model.addAttribute("page", "fragments.html :: user-panel");
        model.addAttribute("userPanelPage", "fragments.html :: user-panel-workout-details");
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
                (userService.findUserByEmail(UserService.getLoggedInUsersEmail()));
        System.out.println(workoutDeserialized.getFiles());
        workoutService.saveWorkout(workoutDeserialized);
    }

    @ResponseBody
    @DeleteMapping("/{workoutId}")
    public void deleteWorkout(@PathVariable long workoutId) {
        workoutService.deleteWorkout(workoutId);
    }
}