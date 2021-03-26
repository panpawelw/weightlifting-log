package com.panpawelw.weightliftinglog;

import static org.assertj.core.api.Assertions.assertThat;

import com.panpawelw.weightliftinglog.controllers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private HomeController homeController;

    @Autowired
    private UserController userController;

    @Autowired
    private AdminController adminController;

    @Autowired
    private WorkoutController workoutController;

    @Autowired
    private ChangePasswordController changePasswordController;

    @Test
    public void contextLoads() {
        assertThat(homeController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(adminController).isNotNull();
        assertThat(workoutController).isNotNull();
        assertThat(changePasswordController).isNotNull();
    }
}
