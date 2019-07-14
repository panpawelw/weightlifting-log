package pl.pjm77.weightliftinglog.models;

import java.util.ArrayList;
import java.util.List;

public class Set {

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
}
