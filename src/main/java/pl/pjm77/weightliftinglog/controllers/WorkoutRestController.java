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
public class WorkoutRestController {

    private final WorkoutService workoutService;

    public WorkoutRestController(WorkoutService workoutService) {this.workoutService = workoutService;}

    @GetMapping("/")
    public List<WorkoutDeserialized> getWorkoutsList() {
        return new ArrayList<>();
    }

    @GetMapping("/{workoutId}")
    public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
        return new WorkoutDeserialized();
    }

    @PostMapping("/add")
    public void addWorkoutPost(@RequestBody WorkoutDeserialized workoutDeserialized,
                        HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Receiving workout...");
        System.out.println(workoutDeserialized);
    }

    @PutMapping("/update/{workoutId}")
    public void updateWorkout(@PathVariable long workoutId, @RequestBody()//required = true)
            WorkoutDeserialized workoutDeserialized) {
    }

    @DeleteMapping("/{workoutId}")
    public void deleteWorkout(@PathVariable long workoutId) {
    }
}