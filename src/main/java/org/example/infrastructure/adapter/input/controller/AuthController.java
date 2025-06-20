package org.example.infrastructure.adapter.input.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.service.UserService;
import org.example.infrastructure.adapter.input.dto.requests.LoginRequest;
import org.example.infrastructure.adapter.input.dto.requests.RefreshTokenRequest;
import org.example.infrastructure.adapter.input.dto.requests.SignUpUserDto;
import org.example.infrastructure.adapter.input.dto.responses.LoginResponse;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpUserDto dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}
