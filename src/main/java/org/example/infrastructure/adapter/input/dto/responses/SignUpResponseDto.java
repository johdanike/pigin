package org.example.infrastructure.adapter.input.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpResponseDto {
    private String message;
    private String userId;
    private String fullName;
}
