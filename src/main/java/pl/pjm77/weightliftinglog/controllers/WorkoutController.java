package pl.pjm77.weightliftinglog.controllers;

import org.springframework.stereotype.Controller;
import pl.pjm77.weightliftinglog.services.WorkoutService;

@Controller
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {this.workoutService = workoutService;}
}