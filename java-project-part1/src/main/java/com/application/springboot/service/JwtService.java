package com.application.springboot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Calendar;
import java.util.Date;

@Service
public class JwtService {

  public Date getExpirationDate() {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_MONTH, 30); // Adds 30 days to the current date
    Date expirationDate = c.getTime();
    System.out.println("Expiration date: " + expirationDate);
    return expirationDate;
  }

  SecretKey secretKey = Jwts.SIG.HS256.key().build();
  String secretString = Encoders.BASE64.encode(secretKey.getEncoded()); // to save above secret key in Base64 (or Base64URL) format encode it
  SecretKey decodedKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));

  public String buildToken(String userEmail) {
    Date currentDate = new Date(); // Calendar.getInstance().getTime();
    String jws = Jwts.builder().subject(userEmail).signWith(secretKey).issuedAt(currentDate).expiration(getExpirationDate()).compact();
    System.out.println("signed JWT(JWS): " + jws);
    return jws;
  }

  public String extractUserEmail(String jwtToken) throws AccessDeniedException {
    return getClaims(jwtToken).getSubject();
  }

  private Claims getClaims(String token) throws AccessDeniedException {
    try {
      Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
      return claims;
    } catch (SignatureException |
             ExpiredJwtException e) {  // invalid signature or expired token
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  private Boolean isTokenExpired(String token) throws AccessDeniedException {
    Claims claims = getClaims(token);
    return claims.getExpiration().before(new Date()); // check if token is expired before current date
  }

  // match email and verify token expiration
  public Boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      String email = extractUserEmail(token);
      return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    } catch (JwtException | AccessDeniedException e) {
      System.out.println("Invalid JWT signature: " + e.getMessage());
      return false;
    }
  }
}
