package com.panpawelw.weightliftinglog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling

public class WeightliftingLogApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WeightliftingLogApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(WeightliftingLogApplication.class, args);
  }

  @Bean
  public TransactionalEmailsApi transactionalEmailsApi() {
    return new TransactionalEmailsApi();
  }

  @Bean
  public SendSmtpEmailSender sendSmtpEmailSender() {
    return new SendSmtpEmailSender();
  }

  @Bean
  public SendSmtpEmailTo sendSmtpEmailTo() {
    return new SendSmtpEmailTo();
  }

  @Bean
  public SendSmtpEmail sendSmtpEmail() {
    return new SendSmtpEmail();
  }
}