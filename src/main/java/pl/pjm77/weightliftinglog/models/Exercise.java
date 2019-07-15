package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exercise implements Serializable {

    private List<Set> sets = new ArrayList<>();
    private List<Note> exerciseNotes = new ArrayList<>();

    public Exercise() {}

    public Exercise(List<Set> sets, List<Note> exerciseNotes) {
        this.sets = sets;
        this.exerciseNotes = exerciseNotes;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
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
                "sets=" + sets +
                ", exerciseNotes=" + exerciseNotes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(getSets(), exercise.getSets()) &&
                Objects.equals(getExerciseNotes(), exercise.getExerciseNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSets(), getExerciseNotes());
    }
}