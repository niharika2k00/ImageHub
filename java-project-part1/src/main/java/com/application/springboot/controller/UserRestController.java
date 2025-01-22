package com.application.springboot.controller;

import com.application.sharedlibrary.entity.Role;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.service.UserService;
import com.application.sharedlibrary.util.EmailTemplateProcessor;
import com.application.springboot.dto.LoginRequestDto;
import com.application.springboot.dto.PasswordUpdateRequestDto;
import com.application.springboot.dto.UserLoginResponseDto;
import com.application.springboot.dto.UserUpdateRequestDto;
import com.application.springboot.exception.ResourceAlreadyExistsException;
import com.application.springboot.service.JwtService;
import com.application.springboot.service.KafkaProducerService;
import com.application.springboot.service.RoleService;
import com.application.springboot.service.UserUpdateServiceImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestController {

  // Autowired to inject service bean into controller, serve as an intermediary between C and DAO layer
  private final UserService userService;
  private final RoleService roleService;
  private final JwtService jwtService;
  private final KafkaProducerService kafkaProducerService;
  private final UserUpdateServiceImpl userUpdateServiceImpl;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final EmailTemplateProcessor emailTemplateProcessor;

  @Autowired
  public UserRestController(UserService userService, RoleService roleService, JwtService jwtService, KafkaProducerService kafkaProducerService, UserUpdateServiceImpl userUpdateServiceImpl, KafkaTemplate<String, String> kafkaTemplate, EmailTemplateProcessor emailTemplateProcessor) {
    this.userService = userService;
    this.roleService = roleService;
    this.jwtService = jwtService;
    this.kafkaProducerService = kafkaProducerService;
    this.userUpdateServiceImpl = userUpdateServiceImpl;
    this.kafkaTemplate = kafkaTemplate;
    this.emailTemplateProcessor = emailTemplateProcessor;
  }

  // for kafka testing
  @GetMapping("/sendkafka")
  public String testKafka(@RequestParam String msg) throws Exception {
    kafkaProducerService.sendMsgToTopic(msg);
    //return "msg send successfully";
    return msg;
  }

  // GET all /users
  @GetMapping("/users")
  public List<User> findAll() {
    return userService.findAll();
  }

  // GET /users/{id}
  @GetMapping("/users/{id}")
  // can throw parent(Exception) class instead of comma separated multiple exceptions
  public User findById(@PathVariable int id) throws Exception {
    User userDetails = userService.findById(id);
    return userDetails;
  }

  // POST /users/register - SignUp | add new user
  @PostMapping("/users/register")
  public User addNewUser(@RequestBody User user) throws Exception {
    // Check: email already exist
    User isUserExist = userService.findByEmail(user.getEmail());
    if (isUserExist != null) {
      throw new ResourceAlreadyExistsException("Email already exists.");
    }

    user.setId(0);

    // Password encryption
    String plainTextPassword = user.getPassword();
    String salt = BCrypt.gensalt(12);
    String hashedPassword = BCrypt.hashpw(plainTextPassword, salt);
    user.setPassword(hashedPassword);
    System.out.println("hashed password: " + hashedPassword);

    // Grant basic user role
    Role basicAuthority = roleService.findByRoleName("ROLE_USER");
    user.setRoles(Set.of(basicAuthority));

    // Save user object to DB
    User newUserObject = userUpdateServiceImpl.saveOrUpdate(user);
    System.out.println("Success! New user registered. " + newUserObject);

    // Sending email
    Path path = Paths.get("./user_welcome_email.md");
    String mailBodyMd = Files.readString(path);

    // Mapping placeholders for replacement
    Map<String, String> replacements = Map.of(
      "{{username}}", newUserObject.getName().toUpperCase()
    );
    String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements); // convert markdown content to html

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("subject", "Welcome to Image Hub! Your Account Has Been Successfully Created");
    jsonPayload.put("body", mailBodyHtml);
    jsonPayload.put("receiverEmail", newUserObject.getEmail());

    kafkaTemplate.send("testtopic2", jsonPayload.toJSONString());

    return newUserObject;
  }

  // POST /users/login - Login existing user
  @PostMapping("/users/login")
  public UserLoginResponseDto loginUser(@RequestBody LoginRequestDto reqBody) throws CustomResourceNotFoundException { // use {email, password} for now, later replace with {username, password}
    System.out.println(reqBody);
    UserLoginResponseDto userLoginResponse = null;

    User userInfo = userService.findByEmail(reqBody.getEmail());
    String inputPassword = reqBody.getPassword(); // text
    String originalPassword = userInfo.getPassword(); // hashed

    // Salt is already stored as a prefix in the hashed password in database
    if (BCrypt.checkpw(inputPassword, originalPassword)) {
      System.out.println("Successfully logged in");
      String jwtToken = jwtService.buildToken(userInfo.getEmail()); // authenticated users email
      userLoginResponse = new UserLoginResponseDto(Optional.of(jwtToken), Optional.of(jwtService.getExpirationDate()), "Token generated successfully!");
    } else {
      System.out.println("Incorrect email or password");
      userLoginResponse = new UserLoginResponseDto(Optional.empty(), Optional.empty(), "Incorrect email or password");
    }

    return userLoginResponse;
  }

  // POST /users/logout
  @PostMapping("/users/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.badRequest().body("Invalid token.");
    }

    String token = authHeader.substring(7);
    long expirationTime = jwtService.getExpirationDate().getTime();
    jwtService.addTokenToBlacklist(token, expirationTime);

    System.out.println("Successfully logged out.");
    return ResponseEntity.ok("Successfully logged out.");
  }

  // PUT /users - update existing user except password
  @PutMapping("/users")
  public String updateUser(@RequestBody UserUpdateRequestDto updatedUser) throws Exception {
    userUpdateServiceImpl.updateUser(updatedUser);
    return "User updated successfully";
  }

  // PUT /users/auth - update user's password
  @PutMapping("/users/auth")
  public String updatePassword(@RequestBody PasswordUpdateRequestDto requestBody) throws Exception {
    String msg = userUpdateServiceImpl.updatePassword(requestBody);
    return msg;
  }

  // DELETE /users/{id}
  @DeleteMapping("/users/{id}")
  public String deleteUser(@PathVariable int id) throws Exception {
    userService.deleteById(id);
    System.out.println("Successfully deleted user with id " + id);
    return "Successfully deleted user with id " + id;
  }
}
