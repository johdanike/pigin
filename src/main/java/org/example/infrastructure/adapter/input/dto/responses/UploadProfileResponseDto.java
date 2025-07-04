package org.example.infrastructure.adapter.input.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadProfileResponseDto {
    private boolean success;
    private String message;
    private int creditScore;
    private String userId;

}
