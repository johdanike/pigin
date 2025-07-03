package org.example.application.port.input;

import org.example.infrastructure.adapter.input.dto.requests.UploadProfileRequestDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfileResponseDto;
import org.example.infrastructure.adapter.output.entity.ProfileDataEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UpdateUserProfileUseCase {
    UploadProfilePicResponseDto uploadProfilePicture(String userId, MultipartFile file);
    UploadProfileResponseDto submitProfileData(UploadProfileRequestDto uploadProfileRequest);
    Optional<ProfileDataEntity> getProfileDataByUserId(String userId);
    boolean deleteProfileData(String userId);

    UploadProfileResponseDto updateProfileData(String userId, UploadProfileRequestDto updatedProfileRequest);

    Object getFileBy(String blobId);

//    Object getFileBy(String blobId);
}
