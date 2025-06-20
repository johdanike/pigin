package org.example.infrastructure.adapter.input.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}
