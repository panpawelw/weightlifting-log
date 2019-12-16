package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Set is the most basic training unit. It represents one set of particular
 * exercise.
 *
 * data - contains data related to this particular set, like number of
 *        repetitions performed, time it took, distance covered etc
 * notes - list of notes related to this exercise
 */
public class Set implements Serializable {

    private String data;
    private List<Note> notes = new ArrayList<>();

    public Set() {}

    public Set(String setData, List<Note> notes) {
        this.data = setData;
        this.notes = notes;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Set{" +
                "data='" + data + '\'' +
                ", notes=" + notes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set set = (Set) o;
        return Objects.equals(getData(), set.getData()) &&
                Objects.equals(getNotes(), set.getNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getNotes());
    }
}