package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutDeserialized implements Serializable {

    private Long id;
    private String title;
    private Timestamp created;
    private Timestamp updated;
    private User user;
    private List<Exercise> exercises = new ArrayList<>();
    private List<Note> workoutNotes = new ArrayList<>();

    public WorkoutDeserialized() {}

    public WorkoutDeserialized(Long id, String title, Timestamp created, Timestamp updated,
                               User user, List<Exercise> exercises, List<Note> workoutNotes) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.updated = updated;
        this.user = user;
        this.exercises = exercises;
        this.workoutNotes = workoutNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Note> getWorkoutNotes() {
        return workoutNotes;
    }

    public void setWorkoutNotes(List<Note> workoutNotes) {
        this.workoutNotes = workoutNotes;
    }

    @Override
    public String toString() {
        return "WorkoutDeserialized{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", user=" + user +
                ", exercises=" + exercises +
                ", workoutNotes=" + workoutNotes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutDeserialized)) return false;
        WorkoutDeserialized that = (WorkoutDeserialized) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getCreated(), that.getCreated()) &&
                Objects.equals(getUpdated(), that.getUpdated()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getExercises(), that.getExercises()) &&
                Objects.equals(getWorkoutNotes(), that.getWorkoutNotes());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(), getTitle(), getCreated(), getUpdated(), getUser(), getExercises(), getWorkoutNotes());
    }
}
