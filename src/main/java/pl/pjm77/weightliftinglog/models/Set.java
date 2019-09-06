package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Set implements Serializable {

    private String setData;
    private List<Note> notes = new ArrayList<>();

    public Set() {}

    public Set(String setData, List<Note> notes) {
        this.setData = setData;
        this.notes = notes;
    }

    public String getSetData() {
        return setData;
    }

    public void setSetData(String setData) {
        this.setData = setData;
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
                "setData='" + setData + '\'' +
                ", setNotes=" + notes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set set = (Set) o;
        return Objects.equals(getSetData(), set.getSetData()) &&
                Objects.equals(getNotes(), set.getNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSetData(), getNotes());
    }
}