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

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SerializationDeserializationTests {

    @Autowired
    private WorkoutService workoutService;

    @Test
    public void serializationDeserializationTest() {
        /* set 1 exercise 1 */
        Note set1exercise1Note1 = new Note(0,"First set of first exercise - first note.");
        Note set1exercise1Note2 = new Note(0, "First set of first exercise - second note.");
        List<Note> set1exercise1Notes = new ArrayList<>();
        set1exercise1Notes.add(set1exercise1Note1);
        set1exercise1Notes.add(set1exercise1Note2);
        Set set1exercise1 = new Set("First set first exercise!",set1exercise1Notes);

        /* set 2 exercise 1 */
        Note set2exercise1Note1 = new Note(0,"Second set of first exercise first note.");
        Note set2exercise1Note2 = new Note(0, "Second set of first exercise second note.");
        List<Note> set2exercise1Notes = new ArrayList<>();
        set2exercise1Notes.add(set2exercise1Note1);
        set2exercise1Notes.add(set2exercise1Note2);
        Set set2exercise1 = new Set("Second set first exercise!",set2exercise1Notes);

        /* exercise 1 */
        List<Set> exercise1Sets = new ArrayList<>();
        exercise1Sets.add(set1exercise1);
        exercise1Sets.add(set2exercise1);
        Note exercise1Note = new Note(0, "First exercise note.");
        List<Note> exercise1Notes = new ArrayList<>();
        exercise1Notes.add(exercise1Note);
        Exercise exercise1 = new Exercise("First exercise", exercise1Sets, exercise1Notes);

        /* set 1 exercise 2 */
        Note set1exercise2Note1 = new Note(0,"First set of second exercise - first note.");
        Note set1exercise2Note2 = new Note(0, "First set of second exercise - second note.");
        List<Note> set1exercise2Notes = new ArrayList<>();
        set1exercise2Notes.add(set1exercise2Note1);
        set1exercise2Notes.add(set1exercise2Note2);
        Set set1exercise2 = new Set("First set second exercise!",set1exercise2Notes);

        /* set 2 exercise 2 */
        Note set2exercise2Note1 = new Note(0,"Second set of second exercise first note.");
        Note set2exercise2Note2 = new Note(0, "Second set of second exercise second note.");
        List<Note> set2exercise2Notes = new ArrayList<>();
        set2exercise2Notes.add(set2exercise2Note1);
        set2exercise2Notes.add(set2exercise2Note2);
        Set set2exercise2 = new Set("Second set second exercise!",set2exercise2Notes);

        /* exercise 2 */
        List<Set> exercise2Sets = new ArrayList<>();
        exercise2Sets.add(set1exercise2);
        exercise2Sets.add(set2exercise2);
        Note exercise2Note = new Note(0, "Second exercise note.");
        List<Note> exercise2Notes = new ArrayList<>();
        exercise2Notes.add(exercise2Note);
        Exercise exercise2 = new Exercise("Second exercise", exercise2Sets, exercise2Notes);

        /* workout deserialized */
        List<Exercise> workout1Exercises = new ArrayList<>();
        workout1Exercises.add(exercise1);
        workout1Exercises.add(exercise2);
        Note workout1Note = new Note(0, "First workout note.");
        List<Note> workout1Notes = new ArrayList<>();
        workout1Notes.add(workout1Note);
        User user1 = new User();
        WorkoutDeserialized workout1Deserialized = new WorkoutDeserialized(1L,"First workout title",
                null, null, user1, workout1Exercises, workout1Notes, null);
        WorkoutSerialized workout1Serialized =
                workoutService.serializeWorkout(workout1Deserialized);
        WorkoutDeserialized workout2Deserialized =
                workoutService.deserializeWorkout(workout1Serialized);
        assertEquals(workout1Deserialized, workout2Deserialized);
    }
}
