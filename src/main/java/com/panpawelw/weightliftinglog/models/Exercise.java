package com.panpawelw.weightliftinglog.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Single exercise consists of title and any number of sets, also any number of notes can be
 * attached to it.
 * <p>
 * title - exercise title
 * sets - list of sets of the exercise
 * notes - list of notes attached to this exercise
 */
public class Exercise implements Serializable {

  private String title;
  private List<Set> sets;
  private List<Note> notes;

  public Exercise() {
  }

  public Exercise(String title, List<Set> sets, List<Note> notes) {
    this.title = title;
    this.sets = sets;
    this.notes = notes;
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

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "Exercise{" +
        "title='" + title + '\'' +
        ", sets=" + sets +
        ", exerciseNotes=" + notes +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Exercise)) return false;
    Exercise exercise = (Exercise) o;
    return Objects.equals(getTitle(), exercise.getTitle()) &&
        Objects.equals(getSets(), exercise.getSets()) &&
        Objects.equals(getNotes(), exercise.getNotes());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTitle(), getSets(), getNotes());
  }
}