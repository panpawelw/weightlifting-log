package pl.pjm77.weightliftinglog.models;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workoutId;

    private String filename;

    @Lob
    private byte[] content;

    public File() {
    }

    public File(Long id, Long workoutId, String filename, byte[] content) {
        this.id = id;
        this.workoutId = workoutId;
        this.filename = filename;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}