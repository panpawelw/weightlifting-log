package pl.pjm77.weightliftinglog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.models.WorkoutSerialized;
import pl.pjm77.weightliftinglog.repositories.WorkoutRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<WorkoutSerialized> findWorkoutsByUser(User user) {
        List<WorkoutSerialized> usersWorkoutsSerialized =
                workoutRepository.findAllByUserOrderByCreatedDesc(user);
        return usersWorkoutsSerialized;
    }

//    public List<WorkoutDeserialized> findWorkoutsByUser(User user) {
//        List<WorkoutSerialized> usersWorkoutsSerialized;
//        usersWorkoutsSerialized = workoutRepository.findAllByUserOrderByCreatedDesc(user);
//        List<WorkoutDeserialized> usersWorkoutsDeserialized = new ArrayList<>();
//        usersWorkoutsSerialized.forEach((n) -> usersWorkoutsDeserialized.add(deserializeWorkout(n)));
//        return usersWorkoutsDeserialized;
//    }

    public WorkoutDeserialized findWorkoutById(long id) {
        WorkoutSerialized workoutSerialized = workoutRepository.getOne(id);
        return deserializeWorkout(workoutSerialized);
    }

    public void saveWorkout(WorkoutDeserialized workoutDeserialized) {
        workoutRepository.saveAndFlush(serializeWorkout(workoutDeserialized));
    }

    public WorkoutSerialized serializeWorkout
            (WorkoutDeserialized workoutDeserialized) {
        WorkoutSerialized workoutSerialized = new WorkoutSerialized();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(workoutDeserialized);
            out.flush();
            workoutSerialized.setData(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        workoutSerialized.setId(workoutDeserialized.getId());
        workoutSerialized.setTitle(workoutDeserialized.getTitle());
        workoutSerialized.setCreated(workoutDeserialized.getCreated());
        workoutSerialized.setUpdated(workoutDeserialized.getUpdated());
        workoutSerialized.setUser(workoutDeserialized.getUser());
        return workoutSerialized;
    }

    public WorkoutDeserialized deserializeWorkout
            (WorkoutSerialized workoutSerialized) {
        WorkoutDeserialized workoutDeserialized = new WorkoutDeserialized();
        ByteArrayInputStream bis = new ByteArrayInputStream(workoutSerialized.getData());
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            workoutDeserialized = (WorkoutDeserialized) o;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workoutDeserialized;
    }
}
