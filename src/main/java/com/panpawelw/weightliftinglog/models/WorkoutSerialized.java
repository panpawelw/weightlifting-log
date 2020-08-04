package com.panpawelw.weightliftinglog.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;


/**
 * Workout serialized is ready to be stored in database. Id, title, user, created and updated
 * timestamps are duplicated for search purposes. Data byte array is used to store serialized
 * WorkoutDeserialized object.
 */
@Entity
@Table(name = "workouts")
public class WorkoutSerialized {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp updated;
    @Lob
    private byte[] data;

    @JoinColumn
    @ManyToOne
    private User user;

    public WorkoutSerialized() {}

    public WorkoutSerialized(String title, Timestamp created, Timestamp updated, byte[] data,
                             User user) {
        this.title = title;
        this.created = created;
        this.updated = updated;
        this.data = data;
        this.user = user;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WorkoutSerialized{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", data=" + Arrays.toString(data) +
                ", user=" + user +
                '}';
    }
}
