package pl.pjm77.weightliftinglog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WeightliftingLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeightliftingLogApplication.class, args);
	}
}