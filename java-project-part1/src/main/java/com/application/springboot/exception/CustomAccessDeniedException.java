package com.application.springboot.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedException implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 status code for access denied
    response.getWriter().write("Access Denied: You do not have required permission to access this resource.");
    response.getWriter().flush();
  }
}
