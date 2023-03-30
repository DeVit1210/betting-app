package com.betting.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
// TODO: set @ConfigurationProperties
@Service
public class JwtUtils {
   public String getTokenHeader() {
       return "Bearer ";
   }
   public String getAuthHeader() {
       return HttpHeaders.AUTHORIZATION;
   }

   // TODO: create safer key
   private String getSigningKey() {
       return "keykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykeykey" +
               "keykeykeykeykeykeykeykeykeykeykeykeykeykey";
   }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUsername(String token) {
       return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
       return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
       final Map<String, Object> claims = new HashMap<>();
       return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
       return Jwts.builder()
               .setClaims(claims)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24))
               .signWith(Keys.hmacShaKeyFor(getSigningKey().getBytes()), SignatureAlgorithm.HS256)
               .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
       final String username = extractUsername(token);
       return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
       return extractExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }


    private Claims extractAllClaims(String token) {
       return Jwts.parserBuilder()
               .setSigningKey(getSigningKey().getBytes())
               .build()
               .parseClaimsJws(token)
               .getBody();
    }
}
