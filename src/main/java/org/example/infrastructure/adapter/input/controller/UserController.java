package org.example.infrastructure.adapter.input.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.input.UpdateUserProfileUseCase;
import org.example.domain.service.UserService;
import org.example.infrastructure.adapter.input.dto.requests.UploadProfileRequestDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfileResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final UpdateUserProfileUseCase updateUserProfileUseCase;


//    @PostMapping("/uploadProfile")
//    public ResponseEntity<UploadProfileResponseDto> submitProfile(@RequestBody UploadProfileRequestDto dto, Principal principal) {
//        log.info("Authenticated user = {}", principal.getName());
//        return ResponseEntity.ok(userService.submitProfileData(dto));
//    }
//
//    @PostMapping("/uploadSelfie")
//    public ResponseEntity<UploadProfilePicResponseDto> uploadSelfie(@RequestParam("file") MultipartFile file,
//                                                                    Principal principal) {
//        String userId = principal.getName();
//        return ResponseEntity.ok(userService.uploadProfilePicture(userId, file));
//    }

    @PostMapping("/{userId}/upload-picture")
    public ResponseEntity<UploadProfilePicResponseDto> uploadProfilePicture(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file
    ) {
        UploadProfilePicResponseDto response = updateUserProfileUseCase.uploadProfilePicture(userId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = {MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> getMediaFile(@RequestParam(value = "blobId", required = false) String blobId) {
       return ResponseEntity.status(HttpStatus.OK).body(userService.getFileBy(blobId));
    }

    @PostMapping("/{userId}/profile")
    public ResponseEntity<UploadProfileResponseDto> uploadProfileData(
            @PathVariable String userId,
            @RequestBody UploadProfileRequestDto uploadRequest
    ) {
        uploadRequest.setUserId(userId);
        UploadProfileResponseDto response = updateUserProfileUseCase.submitProfileData(uploadRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<UploadProfileResponseDto> updateProfileData(
            @PathVariable String userId,
            @RequestBody UploadProfileRequestDto updatedProfileRequest
    ) {
        UploadProfileResponseDto response = updateUserProfileUseCase.updateProfileData(userId, updatedProfileRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getProfileData(@PathVariable String userId) {
        return updateUserProfileUseCase.getProfileDataByUserId(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}/profile")
    public ResponseEntity<Void> deleteProfileData(@PathVariable String userId) {
        boolean deleted = updateUserProfileUseCase.deleteProfileData(userId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
