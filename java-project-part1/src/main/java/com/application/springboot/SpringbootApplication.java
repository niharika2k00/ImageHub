package com.application.springboot;

import com.application.springboot.entity.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.application")
@EnableJpaRepositories("com.application")
@EntityScan("com.application")
public class SpringbootApplication {

  public static void main(String[] args) {
    System.out.println("Project started 👀...");

    //createRoleTable();
    SpringApplication.run(SpringbootApplication.class, args);
  }

  // Add all roles in the database at the beginning of the application
  public static void createRoleTable() {
    List<Role> roles = new ArrayList<>();

    roles.add(new Role("ROLE_ADMIN"));
    roles.add(new Role("ROLE_DEVELOPER"));
    roles.add(new Role("ROLE_EDITOR"));
    roles.add(new Role("ROLE_GUEST"));
    roles.add(new Role("ROLE_MANAGER"));
    roles.add(new Role("ROLE_OPERATOR"));
    roles.add(new Role("ROLE_SUPER_ADMIN"));
    roles.add(new Role("ROLE_USER"));
    roles.add(new Role("ROLE_VIEWER"));
  }
}
