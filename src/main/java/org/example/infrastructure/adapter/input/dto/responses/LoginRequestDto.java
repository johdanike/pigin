package org.example.infrastructure.adapter.input.dto.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
