package org.example.infrastructure.adapter.input.implemetation;

import org.example.application.port.output.UserRepositoryUseCase;
import org.example.domain.model.User;
import org.example.domain.service.UserService;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.output.walrus.WalrusCloudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootTest(properties = {
        "walrus.upload.url=http://localhost:9000/api/v1/upload",
        "walrus.download.url=http://localhost:9000/api/v1/file/",
        "walrus.address=test-bucket-name",
        "walrus.epochs=1"
})
class ProfilePictureServiceTest {

    private WalrusCloudService walrusCloudService;
    private UserRepositoryUseCase userRepository;
    private ProfilePictureService profilePictureService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        walrusCloudService = mock(WalrusCloudService.class);
        userRepository = mock(UserRepositoryUseCase.class);
        profilePictureService = new ProfilePictureService(walrusCloudService, userRepository);
        profilePictureService.getClass().getDeclaredFields();
        try {
            var field = ProfilePictureService.class.getDeclaredField("walrusDownloadUrl");
            field.setAccessible(true);
            field.set(profilePictureService, "http://localhost:9000/media");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUploadProfilePicture() {
        String userId = "user123";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walrusCloudService.upload(any())).thenReturn("abc123");

        MockMultipartFile file = new MockMultipartFile("file", "image.jpg",
                "image/jpeg", "image-data".getBytes());
        UploadProfilePicResponseDto response = profilePictureService.uploadProfilePicture(userId, file);
        assertNotNull(response);
        assertEquals("http://localhost:9000/media/abc123", response.getImageUrl());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image-data".getBytes());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            profilePictureService.uploadProfilePicture("nonexistent", file);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void userCanUploadProfilePicture_alongsideOtherProfileData_test() {
        String userId = "userId";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walrusCloudService.upload(any())).thenReturn("test123");

        byte[] imageContent = "fake-image-content".getBytes();
        MultipartFile file = new MockMultipartFile("image", "test-image.png", "image/png", imageContent);
        
        UploadProfilePicResponseDto response = profilePictureService.uploadProfilePicture(userId, file);
        assertThat(response).isNotNull();
        assertThat(response.getImageUrl()).isNotNull();
        assertThat(response.getImageUrl()).contains("test123");
    }


    @Test
    void testThatUserCanGetFile(){
        // Given
        String blobId = "J9cXuJjM3O71evQWejrzLCeSqYEjqDLErCLeAtHuL3I";
        byte[] mockFileContent = "mock-file-content".getBytes();
        
        // Mock the walrusCloudService.getFileBy method to return mock content
        when(walrusCloudService.getFileBy(blobId)).thenReturn(mockFileContent);
        
        // When
        byte[] fileContent = profilePictureService.getFileBy(blobId);
        
        // Then
        log.info("data: {}", fileContent);
        assertThat(fileContent).isNotNull();
        assertThat(fileContent).isNotEmpty();
        assertThat(fileContent).isEqualTo(mockFileContent);
        
        // Verify the service was called with correct parameters
        verify(walrusCloudService).getFileBy(blobId);
    }

    @Test
    void testCanUploadFile() {
        String fileLocation = "C:\\Users\\DELL USER\\Pictures\\greatlisteners.jpg";
        Path path = Paths.get(fileLocation);
        try (var inputStream = Files.newInputStream(path)) {
            MultipartFile file = new MockMultipartFile(
                    "file", "greatlisteners.jpg", "image/jpeg", inputStream
            );

            String blobId = walrusCloudService.upload(file);
            System.out.println("blob id is " + blobId);

            assertThat(blobId).isNotNull();
            assertThat(blobId).isNotEmpty();

        } catch (IOException exception) {
            exception.printStackTrace();
            fail("Test failed due to IOException: " + exception.getMessage());
        }
    }


    @Test
    void testCanGetfile() {
        String blobId = "kEhIz0Hxm32BYmU4mJyTFcYoDWXXRskzNuKUJYQ1REg";
        byte[] fileContent = walrusCloudService.getFileBy(blobId);
        assertThat(fileContent).isNotNull();
        assertThat(fileContent).isNotEmpty();
    }
}
