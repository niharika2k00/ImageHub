package com.application.springboot.utility;

import com.application.springboot.exception.CustomAccessDeniedException;
import com.application.springboot.service.JwtService;
import com.application.springboot.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserService userService;
  private final CustomAccessDeniedException customAccessDeniedException;

  @Autowired
  public JwtAuthenticationFilter(JwtService jwtService, UserService userService, CustomAccessDeniedException customAccessDeniedException) {
    this.jwtService = jwtService;
    this.userService = userService;
    this.customAccessDeniedException = customAccessDeniedException;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    try {
      String authHeader = request.getHeader("Authorization"); // get bearer token
      String jwtToken = null, userEmail = null;

      System.out.println("Authorization header: " + authHeader);

      // extracting jwt token from authorization header
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUserEmail(jwtToken);
      }

      // If no token present, allow the request to proceed without authentication(for public endpoints) or handled by next filter
      if (jwtToken == null) {
        filterChain.doFilter(request, response);
        return;
      }

      // SecurityContextHolder in spring security stores the details of the current authenticated user
      // create a new SecurityContext instance instead of using SecurityContextHolder.getContext().setAuthentication(authentication) to avoid race conditions across multiple threads
      if (!userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(jwtToken, userDetails)) {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // records remote ip address and session id

          context.setAuthentication(authenticationToken);
          SecurityContextHolder.setContext(context);
        }
      }
    } catch (Exception e) {
      System.out.println("Authentication error: " + e.getMessage());
      customAccessDeniedException.handle(request, response, new AccessDeniedException("Access Denied"));
      SecurityContextHolder.clearContext();
      return;
    }

    filterChain.doFilter(request, response);
  }
}
