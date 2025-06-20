package org.example.infrastructure.adapter.input.dto.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
@RequiredArgsConstructor
public class SignUpResponseDto {
    private String message;
    private String userId;
    private String fullName;

    public SignUpResponseDto(String signedUpSuccessfully, String id, String s) {
        this.message = signedUpSuccessfully;
        this.userId = id;
        this.fullName = s;
    }
}
