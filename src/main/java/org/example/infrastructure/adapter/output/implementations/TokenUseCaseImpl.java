//package org.example.infrastructure.adapter.output.implementations;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.example.application.port.output.TokenUseCase;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class TokenUseCaseImpl implements TokenUseCase {
//
//    private final Key key;
//    private final long jwtExpiration = 1000 * 60 * 60;
//
//    public TokenUseCaseImpl(@Value("${jwt.secret}") String secret) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    @Override
//    public String generateToken(UserDetails userDetails) {
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    @Override
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        String username = extractUsername(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    @Override
//    public String extractUsername(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    private boolean isTokenExpired(String token) {
//        Date expiration = Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//        return expiration.before(new Date());
//    }
//}
