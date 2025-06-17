package org.example.infrastructure.adapter.input.controllers;

import org.example.domain.service.AuthService;
import org.example.infrastructure.adapter.input.dto.responses.JwtResponse;
import org.example.infrastructure.adapter.input.dto.responses.LoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.authenticate(loginRequestDto);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}

