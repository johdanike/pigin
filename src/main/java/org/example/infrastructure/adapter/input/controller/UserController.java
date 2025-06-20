package org.example.infrastructure.adapter.input.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.service.UserService;
import org.example.infrastructure.adapter.input.dto.requests.UploadProfileRequestDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfileResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/user/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/uploadProfile")
    public ResponseEntity<UploadProfileResponseDto> submitProfile(@RequestBody UploadProfileRequestDto dto, Principal principal) {
        log.info("Authenticated user = {}", principal.getName());
        return ResponseEntity.ok(userService.submitProfileData(dto));
    }

    @PostMapping("/uploadSelfie")
    public ResponseEntity<UploadProfilePicResponseDto> uploadSelfie(@RequestParam("file") MultipartFile file,
                                                                    Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(userService.uploadProfilePicture(userId, file));
    }
}
