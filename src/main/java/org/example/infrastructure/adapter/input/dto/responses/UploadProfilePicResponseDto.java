package org.example.infrastructure.adapter.input.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadProfilePicResponseDto {
    private String imageUrl;
    private String message;
}
