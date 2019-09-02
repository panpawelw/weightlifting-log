package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Single exercise consists of title and any number of sets, also any number of notes can be
 * attached to it.
 */
public class Exercise implements Serializable {

    private String title;
    private List<Set> sets = new ArrayList<>();
    private List<Note> exerciseNotes = new ArrayList<>();

    public Exercise() {}

    public Exercise(String title, List<Set> sets, List<Note> exerciseNotes) {
        this.title = title;
        this.sets = sets;
        this.exerciseNotes = exerciseNotes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                "title='" + title + '\'' +
                ", sets=" + sets +
                ", exerciseNotes=" + exerciseNotes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(getTitle(), exercise.getTitle()) &&
                Objects.equals(getSets(), exercise.getSets()) &&
                Objects.equals(getExerciseNotes(), exercise.getExerciseNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSets(), getExerciseNotes());
    }
}