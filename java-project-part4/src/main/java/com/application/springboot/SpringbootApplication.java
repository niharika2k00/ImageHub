package com.application.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan("com.application")
//@EnableJpaRepositories("com.application")
//@EntityScan("com.application")
public class SpringbootApplication {
  public static void main(String[] args) throws NullPointerException {
    System.out.println("Project started 👀...This is kafka consumer");
    SpringApplication.run(SpringbootApplication.class, args);

    // values from env assigned at runtime once the springboot application starts. Need to use @PostConstruct annotation.
  }
}
