package org.example.application.port.input;

import org.example.infrastructure.adapter.input.dto.requests.SignUpUserDto;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;

public interface SignUpUserUseCase {
    SignUpResponseDto signUp(SignUpUserDto signUpUserDto);
}
