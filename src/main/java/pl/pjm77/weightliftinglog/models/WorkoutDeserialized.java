package pl.pjm77.weightliftinglog.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Workout is a basic training unit. It has an Id, title, created and updated timestamps and user
 * it belongs to.
 * Serialization / deserialization is used to allow maximum data structure flexibility - number
 * of sets and notes workout consists of are limited only by ArrayList size limit.
 */
public class WorkoutDeserialized implements Serializable {

    private Long id;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp updated;
    @JsonIgnore
    private User user;
    private List<Exercise> exercises = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<String> filenames = new ArrayList<>();

    public WorkoutDeserialized() {}

    public WorkoutDeserialized(Long id, String title, Timestamp created, Timestamp updated,
                               User user, List<Exercise> exercises, List<Note> notes,
                               List<String> filenames) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.updated = updated;
        this.user = user;
        this.exercises = exercises;
        this.notes = notes;
        this.filenames = filenames;
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

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> files) {
        this.filenames = files;
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
                ", notes=" + notes +
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
                Objects.equals(getNotes(), that.getNotes());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(), getTitle(), getCreated(), getUpdated(), getUser(), getExercises(), getNotes());
    }
}
