package pl.pjm77.weightliftinglog.controllers;

import org.springframework.web.bind.annotation.*;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {this.workoutService = workoutService;}

    @GetMapping("/")
    @ResponseBody
    public List<WorkoutDeserialized> getWorkoutsList() {
        return new ArrayList<>();
    }

    @GetMapping("/{workoutId}")
    @ResponseBody
    public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
        return new WorkoutDeserialized();
    }

    @GetMapping("/add")
    public String addWorkoutGet() {

        return "home";
    }

    @PostMapping("/add")
    public void addWorkoutPost(@RequestBody WorkoutDeserialized workoutDeserialized,
                        HttpServletRequest request, HttpServletResponse response) {
    }

    @PutMapping("/update/{workoutId}")
    @ResponseBody
    public void updateWorkout(@PathVariable long workoutId, @RequestBody()//required = true)
            WorkoutDeserialized workoutDeserialized) {
    }

    @DeleteMapping("/{workoutId}")
    @ResponseBody
    public void deleteWorkout(@PathVariable long workoutId) {
    }
}