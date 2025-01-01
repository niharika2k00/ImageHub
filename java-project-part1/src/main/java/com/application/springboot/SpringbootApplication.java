package com.application.springboot;

import com.application.springboot.entity.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan("com.application")
@ComponentScan(basePackages = {"com.application.springboot", "com.application.sharedlibrary"})
//@Configuration
//@EntityScan("com.application")
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
