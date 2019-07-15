package pl.pjm77.weightliftinglog.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "workouts")
public class WorkoutSerialized {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Timestamp created;

    private Timestamp updated;

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
