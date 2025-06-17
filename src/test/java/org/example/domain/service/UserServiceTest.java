package org.example.domain.service;

import org.example.application.port.output.UserRepositoryUseCase;
import org.example.domain.model.ProfileData;
import org.example.domain.model.User;
import org.example.infrastructure.adapter.input.dto.requests.SignUpUserDto;
import org.example.infrastructure.adapter.input.dto.requests.UploadProfileRequestDto;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.UploadProfileResponseDto;
import org.example.infrastructure.adapter.output.repositories.ProfileDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryUseCase userRepositoryUseCase;
    
    @Mock
    private ProfileDataRepository profileDataRepository;

    @InjectMocks
    private UserService userService;

    private SignUpUserDto signUpUserDto;
    private final String errorMessage = "Fields must not be empty, contain special character or contain whitespace";

    @BeforeEach
    public void setup() {
        signUpUserDto = new SignUpUserDto();
        signUpUserDto.setFirstName("Jane");
        signUpUserDto.setLastName("Doe");
        signUpUserDto.setUserName("jdoe");
        signUpUserDto.setEmail("jane@example.com");
        signUpUserDto.setPassword("password123");
    }

    @Test
    public void testThatSignUpMethod_shouldSaveValidUser() {
        doNothing().when(userRepositoryUseCase).save(any(User.class));
        SignUpResponseDto response = userService.signUp(signUpUserDto);
        assertNotNull(response);
        assertEquals("Signed up successfully", response.getMessage());
        verify(userRepositoryUseCase, times(1)).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldReturnCorrectUserDetails() {
        doNothing().when(userRepositoryUseCase).save(any(User.class));
        SignUpResponseDto response = userService.signUp(signUpUserDto);

        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals("Jane Doe", response.getFullName());
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNullFirstName() {
        signUpUserDto.setFirstName(null);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNullLastName() {
        signUpUserDto.setLastName(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNullEmail() {
        signUpUserDto.setEmail(null);
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNullPassword() {
        signUpUserDto.setPassword(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithEmptyFirstName() {
        signUpUserDto.setFirstName("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithEmptyLastName() {
        signUpUserDto.setLastName("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithEmptyEmail() {
        signUpUserDto.setEmail("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithEmptyPassword() {
        signUpUserDto.setPassword("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithWhitespaceInFirstName() {
        signUpUserDto.setFirstName("Jane Doe");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithWhitespaceInLastName() {
        signUpUserDto.setLastName("Doe Smith");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithWhitespaceInEmail() {
        signUpUserDto.setEmail("jane doe@example.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithWhitespaceInPassword() {
        signUpUserDto.setPassword("password 123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithSpecialCharacterInFirstName() {
        signUpUserDto.setFirstName("Jane!");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithSpecialCharacterInLastName() {
        signUpUserDto.setLastName("Doe@");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNumberInFirstName() {
        signUpUserDto.setFirstName("Jane1");
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldNotSaveUserWithNumberInLastName() {
        signUpUserDto.setLastName("Doe2");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(signUpUserDto)
        );
        assertEquals(errorMessage, exception.getMessage());
        verify(userRepositoryUseCase, never()).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldSaveUserWithSpecialCharactersInEmail() {
        signUpUserDto.setEmail("jane.doe@example-test.com");
        doNothing().when(userRepositoryUseCase).save(any(User.class));

        SignUpResponseDto response = userService.signUp(signUpUserDto);

        assertNotNull(response);
        assertEquals("Signed up successfully", response.getMessage());
        verify(userRepositoryUseCase, times(1)).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldSaveUserWithNumbersInPassword() {
        signUpUserDto.setPassword("password123456");
        doNothing().when(userRepositoryUseCase).save(any(User.class));
        SignUpResponseDto response = userService.signUp(signUpUserDto);

        assertNotNull(response);
        assertEquals("Signed up successfully", response.getMessage());
        verify(userRepositoryUseCase, times(1)).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldSaveUserWithSpecialCharactersInPassword() {
        signUpUserDto.setPassword("P@ssw0rd!");
        doNothing().when(userRepositoryUseCase).save(any(User.class));
        SignUpResponseDto response = userService.signUp(signUpUserDto);

        assertNotNull(response);
        assertEquals("Signed up successfully", response.getMessage());
        verify(userRepositoryUseCase, times(1)).save(any(User.class));
    }

    @Test
    public void testThatSignUpMethod_shouldSaveUserWithMinimumLengthFields() {
        signUpUserDto.setFirstName("A");
        signUpUserDto.setLastName("B");
        signUpUserDto.setEmail("a@b.c");
        signUpUserDto.setPassword("pass123");
        doNothing().when(userRepositoryUseCase).save(any(User.class));

        SignUpResponseDto response = userService.signUp(signUpUserDto);
        assertNotNull(response);
        assertEquals("Signed up successfully", response.getMessage());
        verify(userRepositoryUseCase, times(1)).save(any(User.class));
    }
    @Test
    void submitProfileData_shouldStoreAndTriggerScoring() {
        UploadProfileRequestDto uploadProfileRequestDto = new UploadProfileRequestDto();
        uploadProfileRequestDto.setAirtimeTopUpsPerMonth(4);
        uploadProfileRequestDto.setFintechActivity(15);
        uploadProfileRequestDto.setTrainingPrograms(15);
        uploadProfileRequestDto.setUtilityPayments(20);
        uploadProfileRequestDto.setWalletInflow(new BigDecimal("30000"));

        when(profileDataRepository.save(any(ProfileData.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UploadProfileResponseDto response = userService.submitProfileData(uploadProfileRequestDto);
        
        assertTrue(response.isSuccess());
        assertEquals("Profile data submitted successfully", response.getMessage());
        
        verify(profileDataRepository, times(1)).save(any(ProfileData.class));
    }
    
    @Test
    void submitProfileData_shouldCalculateCorrectScore_forHighWalletInflow_test() {
        UploadProfileRequestDto uploadProfileRequestDto = new UploadProfileRequestDto();
        uploadProfileRequestDto.setAirtimeTopUpsPerMonth(4);
        uploadProfileRequestDto.setFintechActivity(15);
        uploadProfileRequestDto.setTrainingPrograms(15);
        uploadProfileRequestDto.setUtilityPayments(20);
        uploadProfileRequestDto.setWalletInflow(new BigDecimal("60000")); // > 50000 = 20 points
        
        ArgumentCaptor<ProfileData> profileDataCaptor = ArgumentCaptor.forClass(ProfileData.class);
        when(profileDataRepository.save(profileDataCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        
        UploadProfileResponseDto response = userService.submitProfileData(uploadProfileRequestDto);
        
        ProfileData savedData = profileDataCaptor.getValue();
        assertEquals(new BigDecimal("60000"), savedData.getMonthlyWalletInflow());
        assertEquals(4, savedData.getAirtimeTopUpsPerMonth());
        assertEquals(20, savedData.getUtilityPaymentScore());
        assertEquals(15, savedData.getFinTechActivityScore());
        assertEquals(15, savedData.getTrainingProgramScore());
        assertEquals(90, savedData.getTotalScore());
        assertEquals(90, response.getCreditScore());
    }
    
    @Test
    void submitProfileData_shouldCalculateCorrectScore_forLowWalletInflow_test() {
        UploadProfileRequestDto uploadProfileRequestDto = new UploadProfileRequestDto();
        uploadProfileRequestDto.setAirtimeTopUpsPerMonth(1);
        uploadProfileRequestDto.setFintechActivity(5);
        uploadProfileRequestDto.setTrainingPrograms(5);
        uploadProfileRequestDto.setUtilityPayments(5);
        uploadProfileRequestDto.setWalletInflow(new BigDecimal("3000"));
        
        ArgumentCaptor<ProfileData> profileDataCaptor = ArgumentCaptor.forClass(ProfileData.class);
        when(profileDataRepository.save(profileDataCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        
        UploadProfileResponseDto response = userService.submitProfileData(uploadProfileRequestDto);
        
        ProfileData savedData = profileDataCaptor.getValue();
        assertEquals(20, savedData.getTotalScore());
        assertEquals(20, response.getCreditScore());
    }
    
    @Test
    void submitProfileData_shouldHandleNullWalletInflow() {
        UploadProfileRequestDto uploadProfileRequestDto = new UploadProfileRequestDto();
        uploadProfileRequestDto.setAirtimeTopUpsPerMonth(4);
        uploadProfileRequestDto.setFintechActivity(15);
        uploadProfileRequestDto.setTrainingPrograms(15);
        uploadProfileRequestDto.setUtilityPayments(20);
        uploadProfileRequestDto.setWalletInflow(null);
        
        when(profileDataRepository.save(any(ProfileData.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UploadProfileResponseDto response = userService.submitProfileData(uploadProfileRequestDto);
        
        assertTrue(response.isSuccess());
        assertEquals("Profile data submitted successfully", response.getMessage());
        verify(profileDataRepository, times(1)).save(any(ProfileData.class));
    }
    
    @Test
    void submitProfileData_shouldHandleMaximumScores_test() {
        UploadProfileRequestDto uploadProfileRequestDto = new UploadProfileRequestDto();
        uploadProfileRequestDto.setAirtimeTopUpsPerMonth(10);
        uploadProfileRequestDto.setFintechActivity(25);
        uploadProfileRequestDto.setTrainingPrograms(25);
        uploadProfileRequestDto.setUtilityPayments(30);
        uploadProfileRequestDto.setWalletInflow(new BigDecimal("100000"));
        
        ArgumentCaptor<ProfileData> profileDataCaptor = ArgumentCaptor.forClass(ProfileData.class);
        when(profileDataRepository.save(profileDataCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        
        UploadProfileResponseDto response = userService.submitProfileData(uploadProfileRequestDto);
        
        ProfileData savedData = profileDataCaptor.getValue();
        assertEquals(120, savedData.getTotalScore());
        assertEquals(120, response.getCreditScore());
    }

    @Test
    void userCanUploadProfilePicture_alongsideOtherProfileData_test() {

    }


}