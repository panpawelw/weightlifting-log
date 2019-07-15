package pl.pjm77.weightliftinglog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pjm77.weightliftinglog.models.*;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SerializationDeserializationTests {

    @Autowired
    private WorkoutService workoutService;

    @Test
    public void SerializationDeserializationTest() throws Exception {
        /* setup exercise 1 set 1*/
        List<Note> exercise1Set1Notes = new ArrayList<>();
        exercise1Set1Notes.add(new Note(0,"First set of first exercise - first note."));
        exercise1Set1Notes.add(new Note(0, "First set of first exercise - second note."));
        Set exercise1Set1 = new Set("First set first exercise!",exercise1Set1Notes);

        /* setup exercise 1 set 2 */
        List<Note> exercise1Set2Notes = new ArrayList<>();
        exercise1Set2Notes.add(new Note(0,"Second set of first exercise first note."));
        exercise1Set2Notes.add(new Note(0, "Second set of first exercise second note."));
        Set exercise1Set2 = new Set("Second set first exercise!",exercise1Set2Notes);

        /* exercise 1 */
        List<Set> exercise1Sets = new ArrayList<>();
        exercise1Sets.add(exercise1Set1);
        exercise1Sets.add(exercise1Set2);
        List<Note> exercise1Notes = new ArrayList<>();
        exercise1Notes.add(new Note(0, "First exercise note."));
        Exercise exercise1 = new Exercise(exercise1Sets, exercise1Notes);

        /* setup exercise 2 set 1*/
        List<Note> exercise2Set1Notes = new ArrayList<>();
        exercise2Set1Notes.add(new Note(0,"First set of first exercise - first note."));
        exercise2Set1Notes.add(new Note(0, "First set of first exercise - second note."));
        Set exercise2Set1 = new Set("First set first exercise!",exercise2Set1Notes);

        /* setup exercise 2 set 2 */
        List<Note> exercise2Set2Notes = new ArrayList<>();
        exercise2Set2Notes.add(new Note(0,"Second set of first exercise first note."));
        exercise2Set2Notes.add(new Note(0, "Second set of first exercise second note."));
        Set exercise2Set2 = new Set("Second set first exercise!",exercise2Set2Notes);

        /* exercise 2 */
        List<Set> exercise2Sets = new ArrayList<>();
        exercise2Sets.add(exercise2Set1);
        exercise2Sets.add(exercise2Set2);
        List<Note> exercise2Notes = new ArrayList<>();
        exercise2Notes.add(new Note(0, "First exercise note."));
        Exercise exercise2 = new Exercise(exercise2Sets, exercise2Notes);

        /* workout deserialized */
        List<Exercise> workout1Exercises = new ArrayList<>();
        workout1Exercises.add(exercise1);
        workout1Exercises.add(exercise2);
        List<Note> workout1Notes = new ArrayList<>();
        workout1Notes.add(new Note(0, "First workout note."));
        User user1 = new User();
        WorkoutDeserialized workout1 = new WorkoutDeserialized(1L,"First workout title",
                null,null, user1, workout1Exercises, workout1Notes);
        System.out.println(workout1.toString());
    }
}
