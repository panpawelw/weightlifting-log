package pl.pjm77.weightliftinglog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pjm77.weightliftinglog.controllers.AdminController;
import pl.pjm77.weightliftinglog.controllers.HomeController;
import pl.pjm77.weightliftinglog.controllers.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTests {

    @Autowired
    private HomeController homeController;

    @Autowired
    private UserController userController;

    @Autowired
    private AdminController adminController;

    @Test
    public void contextLoads() {
        assertThat(homeController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(adminController).isNotNull();
    }
}
