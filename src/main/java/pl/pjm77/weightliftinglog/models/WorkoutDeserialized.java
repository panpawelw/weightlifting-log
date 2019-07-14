package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDeserialized implements Serializable {

    private Long id;
    private String title;
    private Timestamp created;
    private Timestamp updated;
    private User user;
    private List<Exercise> exercises = new ArrayList<>();
    private List<Note> workoutNotes = new ArrayList<>();

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
}
