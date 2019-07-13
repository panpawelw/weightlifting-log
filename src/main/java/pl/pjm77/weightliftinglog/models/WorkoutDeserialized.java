package pl.pjm77.weightliftinglog.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDeserialized implements Serializable {

    private Long id;
    private String title;
    private Timestamp created;
    private Timestamp updated;
    private List<Exercise> exercises = new ArrayList<>();
    private List<Note> workoutNotes = new ArrayList();
}
