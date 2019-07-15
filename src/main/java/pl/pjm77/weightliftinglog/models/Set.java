package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Set implements Serializable {

    private String setData;
    private List<Note> setNotes = new ArrayList<>();

    public Set() {}

    public Set(String setData, List<Note> setNotes) {
        this.setData = setData;
        this.setNotes = setNotes;
    }

    public String getSetData() {
        return setData;
    }

    public void setSetData(String setData) {
        this.setData = setData;
    }

    public List<Note> getSetNotes() {
        return setNotes;
    }

    public void setSetNotes(List<Note> setNotes) {
        this.setNotes = setNotes;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setData='" + setData + '\'' +
                ", setNotes=" + setNotes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set set = (Set) o;
        return Objects.equals(getSetData(), set.getSetData()) &&
                Objects.equals(getSetNotes(), set.getSetNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSetData(), getSetNotes());
    }
}