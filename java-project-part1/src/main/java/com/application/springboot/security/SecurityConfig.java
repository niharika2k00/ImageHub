package com.application.springboot.security;

import com.application.springboot.exception.CustomAccessDeniedException;
import com.application.springboot.utility.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAccessDeniedException customAccessDeniedException;

  @Autowired
  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomAccessDeniedException customAccessDeniedException) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.customAccessDeniedException = customAccessDeniedException;
  }

  // read-only roles
  String[] readAccessRoles = {
    "ROLE_USER",
    "ROLE_VIEWER"
  };

  // editor roles
  String[] writeAccessRoles = {
    "ROLE_DEVELOPER",
    "ROLE_EDITOR",
    "ROLE_MANAGER",
    "ROLE_OPERATOR"
  };

  // admin roles
  String[] adminControlRoles = {
    "ROLE_ADMIN",
    "ROLE_SUPER_ADMIN"
  };

  String[] level1 = Stream.of(readAccessRoles, writeAccessRoles, adminControlRoles).flatMap(Arrays::stream).toArray(String[]::new);
  String[] level2 = Stream.concat(Arrays.stream(writeAccessRoles), Arrays.stream(adminControlRoles)).toArray(String[]::new);

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
      .requestMatchers(HttpMethod.GET, "/", "/api", "/api/test").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/users/register", "/api/users/login", "/api/users/auth").permitAll()
      .requestMatchers(HttpMethod.POST, "/api/users/logout").authenticated()

      .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**", "/api/roles", "/api/roles/**").hasAnyAuthority(level1)

      .requestMatchers(HttpMethod.PUT, "/api/users", "/api/roles").hasAnyAuthority(level2)
      .requestMatchers(HttpMethod.POST, "/api/roles", "/api/upload/image").hasAnyAuthority(level2)

      .requestMatchers(HttpMethod.DELETE, "/api/users/**", "/api/roles/**").hasAnyAuthority(adminControlRoles)
      .requestMatchers(HttpMethod.POST, "/api/users/{id}/roles").hasAnyAuthority(level2)

      .anyRequest().authenticated()
    );

    //http.httpBasic((Customizer.withDefaults())); // enable HTTP basic authentication
    http.httpBasic(e -> e.disable()); // disable HTTP basic authentication
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // add custom filter in the security chain before the existing filter
    http.exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedException)); // handler for access denied exception

    // disable CSRF and CORS. In general not required for stateless REST APIs(POST, PUT, DELETE or PATCH) as don't create/maintain session rather authenticate with jwt token
    http.csrf(csrf -> csrf.disable());
    http.cors(cors -> cors.disable());
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // set session management to stateless

    //http.logout(e -> e.logoutRequestMatcher(new AntPathRequestMatcher("/api/users/logout")).logoutSuccessUrl("/api/users/login"));

    return http.build();
  }
}
