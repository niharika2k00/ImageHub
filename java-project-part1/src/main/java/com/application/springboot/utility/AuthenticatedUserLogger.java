package com.application.springboot.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserLogger {

  //https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html
  public String getLoggedInUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.getPrincipal() instanceof UserDetails info) {
      //UserDetails info = (UserDetails) authentication.getPrincipal();
      System.out.println("user details from global utils: " + info);
      return info.getUsername();
    }

    return null;
  }
}
