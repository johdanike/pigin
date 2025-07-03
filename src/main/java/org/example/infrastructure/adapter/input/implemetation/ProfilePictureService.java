package org.example.infrastructure.adapter.input.implemetation;

import lombok.RequiredArgsConstructor;
import org.example.application.port.output.UserRepositoryUseCase;
import org.example.domain.model.User;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.output.walrus.WalrusCloudService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProfilePictureService {

    private final WalrusCloudService walrusCloudService;
    private final UserRepositoryUseCase userRepository;

    @Value("${walrus.download.url}")
    private String walrusDownloadUrl;

    public UploadProfilePicResponseDto uploadProfilePicture(String userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty or null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String blobId = walrusCloudService.upload(file);
        String fileUrl = String.format("%s/%s", walrusDownloadUrl.replaceAll("/$", ""), blobId);
        user.setProfilePictureUrl(fileUrl);
        userRepository.save(user);

        return getUploadProfilePicResponseDto(fileUrl);
    }

    private static UploadProfilePicResponseDto getUploadProfilePicResponseDto(String fileUrl) {
        UploadProfilePicResponseDto responseDto = new UploadProfilePicResponseDto();
        responseDto.setImageUrl(fileUrl);
        responseDto.setMessage("Profile picture uploaded successfully");
        return responseDto;
    }

    public byte[] getFileBy(String blobId){
        return walrusCloudService.getFileBy(blobId);
    }


}
