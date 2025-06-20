package org.example.application.port.input;

import org.example.infrastructure.adapter.input.dto.requests.LoginRequest;
import org.example.infrastructure.adapter.input.dto.requests.SignUpUserDto;
import org.example.infrastructure.adapter.input.dto.responses.LoginResponse;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;

public interface AuthUseCase {
    SignUpResponseDto signUp(SignUpUserDto signUpUserDto);
    LoginResponse login(LoginRequest loginRequest);

    String refreshAccessToken(String refreshToken);
}
