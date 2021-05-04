package com.panpawelw.weightliftinglog.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MediaFile mediaFile = (MediaFile) o;
    return Objects.equals(id, mediaFile.id) && Objects.equals(workoutId, mediaFile.workoutId)
        && Objects.equals(filename, mediaFile.filename) && Objects.equals(type, mediaFile.type)
        && Arrays.equals(content, mediaFile.content);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, workoutId, filename, type);
    result = 31 * result + Arrays.hashCode(content);
    return result;
  }
}