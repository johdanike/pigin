package org.example.infrastructure.adapter.input.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String userName;
    private String message;
    private Boolean isLoggedIn;
    private String userId;
    private String email;
}
