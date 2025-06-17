package org.example.application.port.output;

import org.springframework.security.core.userdetails.UserDetails;


public interface TokenUseCase {
    String generateToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
