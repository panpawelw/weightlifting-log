package pl.pjm77.weightliftinglog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WeightliftingLogApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WeightliftingLogApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeightliftingLogApplication.class, args);
	}
}