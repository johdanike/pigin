package org.example.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.application.port.input.AuthUseCase;
import org.example.application.port.input.UpdateUserProfileUseCase;
import org.example.application.port.output.UserRepositoryUseCase;
import org.example.domain.model.User;
import org.example.infrastructure.adapter.config.security.JwtUtil;
import org.example.infrastructure.adapter.input.dto.requests.LoginRequest;
import org.example.infrastructure.adapter.input.dto.requests.SignUpUserDto;
import org.example.infrastructure.adapter.input.dto.requests.UploadProfileRequestDto;
import org.example.infrastructure.adapter.input.dto.responses.LoginResponse;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfilePicResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfileResponseDto;
import org.example.infrastructure.adapter.input.implemetation.ProfilePictureService;
import org.example.infrastructure.adapter.output.entity.ProfileDataEntity;
import org.example.infrastructure.adapter.output.repositories.ProfileDataRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements AuthUseCase, UpdateUserProfileUseCase {

    private final UserRepositoryUseCase userRepositoryUseCase;
    private final ProfileDataRepository profileDataRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePictureService profilePictureService;
    private final CreditScoreCalculator creditScoreCalculator;

    public UserService(
            UserRepositoryUseCase userRepositoryUseCase,
            ProfileDataRepository profileDataRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            ProfilePictureService profilePictureService,
            CreditScoreCalculator creditScoreCalculator
    ) {
        this.userRepositoryUseCase = userRepositoryUseCase;
        this.profileDataRepository = profileDataRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.profilePictureService = profilePictureService;
        this.creditScoreCalculator = creditScoreCalculator;
    }

    @Override
    public SignUpResponseDto signUp(SignUpUserDto signUpUserDto) {
        validateSignUpDto(signUpUserDto);
        User user = newUserCreation(signUpUserDto);
        log.info("Password {}", signUpUserDto.getPassword());
        return getSignUpResponseDto(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        checkLoginDetails(loginRequest);
        User foundUser = getExistingUserOrThrow(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), foundUser.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");

        foundUser.setIsLoggedIn(true);
        userRepositoryUseCase.save(foundUser);

        generateAccessAndRefreshTokens result = generateAccessAndRefreshTokens(foundUser);
        return getLoginResponse(result.accessToken(), result.refreshToken(), foundUser);
    }

    private User getExistingUserOrThrow(String username) {
        return userRepositoryUseCase.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }

    private generateAccessAndRefreshTokens generateAccessAndRefreshTokens(User foundUser) {
        log.info("found users username in user service = {}", foundUser.getUserName());
        String accessToken = jwtUtil.generateAccessToken(foundUser.getUserName());
        String refreshToken = jwtUtil.generateRefreshToken(foundUser.getUserName());
        return new generateAccessAndRefreshTokens(accessToken, refreshToken);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        boolean isValid = jwtUtil.validateToken(refreshToken);
        if (!isValid)
            throw new IllegalArgumentException("Invalid refresh token");
        String userId = jwtUtil.getUsernameFromToken(refreshToken);
        return jwtUtil.generateAccessToken(userId);
    }

    private static LoginResponse getLoginResponse(String accessToken, String refreshToken, User user) {
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserName(user.getUserName());
        response.setUserId(String.valueOf(user.getId()));
        response.setEmail(user.getEmail());
        response.setIsLoggedIn(true);
        response.setMessage("Login successful");
        return response;
    }

    private static SignUpResponseDto getSignUpResponseDto(User user) {
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        signUpResponseDto.setUserId(user.getId());
        signUpResponseDto.setMessage("Signed up successfully");
        signUpResponseDto.setFullName(user.getFullName());
        return signUpResponseDto;
    }

    private User newUserCreation(SignUpUserDto signUpUserDto) {
        validateUserDoesNotExist(signUpUserDto.getUserName());

        User user = new User();
        user.setFirstName(signUpUserDto.getFirstName());
        user.setUserName(signUpUserDto.getUserName());
        user.setLastName(signUpUserDto.getLastName());
        user.setEmail(signUpUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
        user.setIsLoggedIn(false);
        return userRepositoryUseCase.save(user);
    }

    private void validateUserDoesNotExist(String username) {
        userRepositoryUseCase.findByUserName(username)
                .ifPresent(u -> { throw new IllegalArgumentException("User already exists"); });
    }

    private void validateSignUpDto(SignUpUserDto signUpUserDto) {
        isRequestNull(signUpUserDto);
        isRequestEmptyOrContainWhiteSpace(signUpUserDto);
        doesRequestContainSpecialCharacter(signUpUserDto);
    }

    private static void isRequestNull(SignUpUserDto signUpUserDto) {
        if (signUpUserDto.getFirstName() == null ||
                signUpUserDto.getLastName() == null ||
                signUpUserDto.getEmail() == null ||
                signUpUserDto.getPassword() == null) {
            throw new IllegalArgumentException("Fields must not be empty, contain special character or contain whitespace");
        }
    }

    private static void isRequestEmptyOrContainWhiteSpace(SignUpUserDto signUpUserDto) {
        if (signUpUserDto.getFirstName().isEmpty() || containsWhiteSpace(signUpUserDto.getFirstName()) ||
                signUpUserDto.getLastName().isEmpty() || containsWhiteSpace(signUpUserDto.getLastName()) ||
                signUpUserDto.getEmail().isEmpty() || containsWhiteSpace(signUpUserDto.getEmail()) ||
                signUpUserDto.getPassword().isEmpty() || containsWhiteSpace(signUpUserDto.getPassword())) {
            throw new IllegalArgumentException("Fields must not be empty, contain special character or contain whitespace");
        }
    }

    private static void doesRequestContainSpecialCharacter(SignUpUserDto signUpUserDto) {
        if (containsSpecialCharacters(signUpUserDto.getFirstName()) ||
                containsSpecialCharacters(signUpUserDto.getLastName())) {
            throw new IllegalArgumentException("Fields must not be empty, contain special character or contain whitespace");
        }
    }

    @Override
    public UploadProfilePicResponseDto uploadProfilePicture(String userId, MultipartFile file) {
        return profilePictureService.uploadProfilePicture(userId, file);
    }

    @Override
    public UploadProfileResponseDto submitProfileData(UploadProfileRequestDto uploadProfileRequest) {
        ProfileDataEntity entity = getProfileDataEntity(uploadProfileRequest);
        getTotalCreditScoreAndPersist(entity);
        return getUploadProfileResponseDto(entity);
    }

    @Override
    public Optional<ProfileDataEntity> getProfileDataByUserId(String userId) {
        return profileDataRepository.findById(userId);
    }

    @Override
    public boolean deleteProfileData(String userId) {
        return false;
    }

    @Override
    public UploadProfileResponseDto updateProfileData(String userId, UploadProfileRequestDto updatedProfileRequest) {
        if (!profileDataRepository.existsById(userId) && userRepositoryUseCase.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        return getUpdatedProfileData(updatedProfileRequest);
    }

    @Override
    public Object getFileBy(String blobId) {
        return profilePictureService.getFileBy(blobId);
    }

    private UploadProfileResponseDto getUpdatedProfileData(UploadProfileRequestDto updatedProfileRequest) {
        ProfileDataEntity profileData = new ProfileDataEntity();
        profileData.setMonthlyWalletInflow(updatedProfileRequest.getWalletInflow());
        profileData.setAirtimeTopUpsPerMonth(updatedProfileRequest.getAirtimeTopUpsPerMonth());
        profileData.setUtilityPaymentScore(updatedProfileRequest.getUtilityPayments());
        profileData.setFinTechActivityScore(updatedProfileRequest.getFintechActivity());
        profileData.setTrainingProgramScore(updatedProfileRequest.getTrainingPrograms());
        profileData.setUserId(updatedProfileRequest.getUserId());

        getTotalCreditScoreAndPersist(profileData);
        return getUploadProfileResponseDto(profileData);
    }

    private ProfileDataEntity getProfileDataEntity(UploadProfileRequestDto uploadProfileRequest) {
        ProfileDataEntity profileData = new ProfileDataEntity();
        profileData.setMonthlyWalletInflow(uploadProfileRequest.getWalletInflow());
        profileData.setAirtimeTopUpsPerMonth(uploadProfileRequest.getAirtimeTopUpsPerMonth());
        profileData.setUtilityPaymentScore(uploadProfileRequest.getUtilityPayments());
        profileData.setFinTechActivityScore(uploadProfileRequest.getFintechActivity());
        profileData.setTrainingProgramScore(uploadProfileRequest.getTrainingPrograms());
        profileData.setUserId(uploadProfileRequest.getUserId());
        return profileData;
    }

    private void getTotalCreditScoreAndPersist(ProfileDataEntity profileData) {
        int totalScore = creditScoreCalculator.calculateTotalScore(profileData);
        profileData.setTotalScore(totalScore);
        profileDataRepository.save(profileData);
    }

    private UploadProfileResponseDto getUploadProfileResponseDto(ProfileDataEntity profileData) {
        UploadProfileResponseDto uploadProfileResponseDto = new UploadProfileResponseDto();
        uploadProfileResponseDto.setSuccess(true);
        uploadProfileResponseDto.setUserId(profileData.getUserId());
        uploadProfileResponseDto.setMessage("Profile data submitted successfully");
        uploadProfileResponseDto.setCreditScore(profileData.getTotalScore());
        return uploadProfileResponseDto;
    }

    public Object getAllUsersForAdmin(int page, int size) {
        return null;
    }

    private record generateAccessAndRefreshTokens(String accessToken, String refreshToken) {}

    private void checkLoginDetails(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty() || containsWhiteSpace(username) ||
                password == null || password.trim().isEmpty() || containsWhiteSpace(password)) {
            throw new IllegalArgumentException("Username or password cannot be empty or contain spaces");
        }
    }

    private static boolean containsWhiteSpace(String input) {
        return input != null && input.matches(".*\\s+.*");
    }

    private static boolean containsSpecialCharacters(String input) {
        return input != null && input.matches(".*[^a-zA-Z0-9].*");
    }
}
