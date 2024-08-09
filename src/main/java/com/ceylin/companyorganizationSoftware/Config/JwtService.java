package com.ceylin.companyorganizationSoftware.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {
  private static final String SECRET_KEY="586b4e7844666b70467d3f492763703f74783a295b6c376f415368534a";
  public String extractEmail(String token) {
    return extractClaim(token,Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
    final Claims claims= extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails){
    return generateToken(new HashMap<>(),userDetails);
  }

  public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
            .signWith(getSigninKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean isTokenValid(String token,UserDetails userDetails){
    final String userEmail= extractEmail(token);
    return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token,Claims::getExpiration);
  }

  private Claims extractAllClaims(String token){
    return Jwts
            .parserBuilder()
            .setSigningKey(getSigninKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSigninKey() {
    byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
