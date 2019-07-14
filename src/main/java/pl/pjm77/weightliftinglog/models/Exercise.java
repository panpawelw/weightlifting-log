package pl.pjm77.weightliftinglog.models;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private List<Exercise> exercises = new ArrayList<>();
    private List<Note> exerciseNotes = new ArrayList<>();

    public Exercise() {}

    public Exercise(List<Exercise> exercises, List<Note> exerciseNotes) {
        this.exercises = exercises;
        this.exerciseNotes = exerciseNotes;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Note> getExerciseNotes() {
        return exerciseNotes;
    }

    public void setExerciseNotes(List<Note> exerciseNotes) {
        this.exerciseNotes = exerciseNotes;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exercises=" + exercises +
                ", exerciseNotes=" + exerciseNotes +
                '}';
    }
}