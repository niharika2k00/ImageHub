package com.application.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.application")
@EnableJpaRepositories("com.application")
@EntityScan("com.application")
public class SpringbootApplication {

  @Autowired
  private static Environment env;

  public SpringbootApplication(Environment env) {
    SpringbootApplication.env = env;
  }

  public static void main(String[] args) throws NullPointerException {
    System.out.println("Project started 👀...This is kafka consumer");
    SpringApplication.run(SpringbootApplication.class, args);

    // the value is assigned at runtime once the springboot application starts
    //String port = env.getProperty("local.server.port");
    //System.out.println("Running in port:" + port);
  }
}
