package com.panpawelw.weightliftinglog.models;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workoutId;

    private String filename;

    private String type;
    @Lob
    private byte[] content;

    public MediaFile() {
    }

    public MediaFile(Long id, Long workoutId, String filename, String type, byte[] content) {
        this.id = id;
        this.workoutId = workoutId;
        this.filename = filename;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}