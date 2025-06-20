package org.example.infrastructure.adapter.input.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;
    private String userId;
}
