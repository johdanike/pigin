package org.example.domain.service;

import org.example.domain.model.Loan;
import org.example.domain.model.enums.LoanStatus;
import org.example.domain.model.User;
import org.example.application.port.output.CreditScoringUseCase;
import org.example.application.port.output.LoanRepositoryUseCase;
import org.example.application.port.output.UserRepositoryUseCase;
import org.example.infrastructure.adapter.input.dto.responses.LoanStatusResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceTest {

    @Mock
    private LoanRepositoryUseCase loanRepositoryUseCase;

    @Mock
    private CreditScoringUseCase creditScoringUseCase;

    @Mock
    private UserRepositoryUseCase userRepositoryUseCase;

    @InjectMocks
    private LoanUseCaseApplicationService loanService;

    private final String userId = "user-001";
    private final BigDecimal amount = new BigDecimal("10000");
    private final int duration = 12;

    @Test
    void applyForLoan_shouldReturnPendingLoan_test() {
        User mockUser = new User();
        mockUser.setFirstName("Daniel");
        mockUser.setLastName("John");
        mockUser.setEmail("john.daniel@gmail.com");
        mockUser.setPhone("+2348123456789");
        mockUser.setBvn("123456789");
        mockUser.setIsLoggedIn(false);
        mockUser.setPassword("password");
        mockUser.setUserName("jd");
        when(userRepositoryUseCase.findById(userId)).thenReturn(Optional.of(mockUser));
        when(creditScoringUseCase.getCreditScore(mockUser)).thenReturn(new BigDecimal("20000"));

        Loan loan = new Loan(userId, amount, 0.12, duration);
        when(loanRepositoryUseCase.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.applyForLoan(userId, amount, duration);

        assertNotNull(result);
        assertEquals(LoanStatus.PENDING, result.getStatus());
        assertEquals(userId, result.getUserId());
        verify(loanRepositoryUseCase, times(1)).save(any(Loan.class));
    }

    @Test
    void applyForLoan_withInvalidAmount_shouldThrowException_test() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loanService.applyForLoan(userId, BigDecimal.ZERO, duration));

        assertEquals("Invalid amount", exception.getMessage());
    }

    @Test
    void applyForLoan_withInvalidDuration_shouldThrowException_test() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loanService.applyForLoan(userId, amount, 0));

        assertEquals("Invalid duration", exception.getMessage());
    }

    @Test
    void testGetLoanStatus_test() {
        // Arrange
        Loan loan = new Loan("user123", BigDecimal.valueOf(1000), 0.12, 12);
        loan.setId("loan123");
        loan.setStatus(org.example.domain.model.enums.LoanStatus.ACTIVE);
        loan.setRemainingAmount(BigDecimal.valueOf(500));
        loan.setEndDate(LocalDate.now().plusMonths(6));

        when(loanRepositoryUseCase.findById("loan123")).thenReturn(java.util.Optional.of(loan));

        // Act
        LoanStatusResponseDto response = loanService.getLoanStatus("loan123");

        // Assert
        assertEquals("loan123", response.getLoanId());
        assertEquals("user123", response.getUserId());
        assertEquals("ACTIVE", response.getStatus());
        assertEquals(BigDecimal.valueOf(1000), response.getOriginalAmount());
        assertEquals(BigDecimal.valueOf(500), response.getRemainingBalance());
        verify(loanRepositoryUseCase, times(1)).findById("loan123");
    }

    @Test
    void testGetLoansByUserId_test() {
        LoanRepositoryUseCase loanRepositoryUseCase = mock(LoanRepositoryUseCase.class);
        LoanUseCaseApplicationService service = new LoanUseCaseApplicationService(loanRepositoryUseCase, null, null);

        Loan loan1 = new Loan("user123", BigDecimal.valueOf(1000), 0.12, 12);
        Loan loan2 = new Loan("user123", BigDecimal.valueOf(2000), 0.10, 24);
        when(loanRepositoryUseCase.findByUserId("user123")).thenReturn(List.of(loan1, loan2));

        List<Loan> loans = service.getLoansByUserId("user123");

        assertEquals(2, loans.size());
        assertEquals(BigDecimal.valueOf(1000), loans.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(2000), loans.get(1).getAmount());
        verify(loanRepositoryUseCase, times(1)).findByUserId("user123");
    }
    @Test
    void testUpdateLoanStatus_test() {
        LoanRepositoryUseCase loanRepositoryUseCase = mock(LoanRepositoryUseCase.class);
        LoanUseCaseApplicationService service = new LoanUseCaseApplicationService(loanRepositoryUseCase, null, null);

        Loan loan = new Loan("user123", BigDecimal.valueOf(1000), 0.12, 12);
        loan.setId("loan123");
        when(loanRepositoryUseCase.findById("loan123")).thenReturn(java.util.Optional.of(loan));

        Object result = service.updateLoanStatus("loan123", "PAID", "Paid off");

        assertEquals("Loan status updated", result);
        assertEquals(LoanStatus.PAID, loan.getStatus());
        verify(loanRepositoryUseCase, times(1)).findById("loan123");
        verify(loanRepositoryUseCase, times(1)).save(loan);
    }
}
