package org.example.infrastructure.adapter.output.implementations;

import org.example.application.port.output.LoanRepositoryUseCase;
import org.example.domain.model.Loan;
import org.example.domain.model.enums.LoanStatus;
import org.example.infrastructure.adapter.output.entity.LoanEntity;
import org.example.infrastructure.adapter.output.entity.UserEntity;
import org.example.infrastructure.adapter.output.repositories.JpaLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanRepositoryUseCaseAdapterTest {

    @Mock
    private JpaLoanRepository jpaLoanRepository;

    @InjectMocks
    private LoanRepositoryUseCaseAdapter loanRepositoryUseCaseAdapter;

    private LoanEntity loanEntity1;
    private LoanEntity loanEntity2;
    private Loan expectedLoan1;
    private Loan expectedLoan2;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // Set up UserEntity
        userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");

        // Set up LoanEntity 1
        loanEntity1 = new LoanEntity();
        loanEntity1.setId("loan-1");
        loanEntity1.setUser(userEntity);
        loanEntity1.setAmount(new BigDecimal("5000.00"));
        loanEntity1.setInterestRate(5.0);
        loanEntity1.setDurationInMonths(12);
        loanEntity1.setStartDate(LocalDate.now());
        loanEntity1.setEndDate(LocalDate.now().plusMonths(12));
        loanEntity1.setStatus(LoanStatus.APPROVED.name());

        // Set up LoanEntity 2
        loanEntity2 = new LoanEntity();
        loanEntity2.setId("loan-2");
        loanEntity2.setUser(userEntity);
        loanEntity2.setAmount(new BigDecimal("10000.00"));
        loanEntity2.setInterestRate(4.5);
        loanEntity2.setDurationInMonths(24);
        loanEntity2.setStartDate(LocalDate.now());
        loanEntity2.setEndDate(LocalDate.now().plusMonths(24));
        loanEntity2.setStatus(LoanStatus.PENDING.name());

        // Set up expected Loan domain objects
        expectedLoan1 = new Loan();
        expectedLoan1.setId("loan-1");
        expectedLoan1.setUserId("1");
        expectedLoan1.setAmount(new BigDecimal("5000.00"));
        expectedLoan1.setInterestRate(5.0);
        expectedLoan1.setDurationInMonths(12);
        expectedLoan1.setStartDate(LocalDate.now());
        expectedLoan1.setEndDate(LocalDate.now().plusMonths(12));
        expectedLoan1.setStatus(LoanStatus.APPROVED);

        expectedLoan2 = new Loan();
        expectedLoan2.setId("loan-2");
        expectedLoan2.setUserId("1");
        expectedLoan2.setAmount(new BigDecimal("10000.00"));
        expectedLoan2.setInterestRate(4.5);
        expectedLoan2.setDurationInMonths(24);
        expectedLoan2.setStartDate(LocalDate.now());
        expectedLoan2.setEndDate(LocalDate.now().plusMonths(24));
        expectedLoan2.setStatus(LoanStatus.PENDING);
    }

    @Test
    void findByUserId_shouldReturnLoansListForValidUserId() {
        // Given
        String userId = "1L";
        List<LoanEntity> loanEntities = Arrays.asList(loanEntity1, loanEntity2);
        when(jpaLoanRepository.findByUser_IdOrderByStartDateDesc(userId)).thenReturn(loanEntities);

        // When
        List<Loan> result = loanRepositoryUseCaseAdapter.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify first loan
        Loan firstLoan = result.get(0);
        assertEquals("loan-1", firstLoan.getId());
        assertEquals("1", firstLoan.getUserId());
        assertEquals(new BigDecimal("5000.00"), firstLoan.getAmount());
        assertEquals(5.0, firstLoan.getInterestRate());
        assertEquals(12, firstLoan.getDurationInMonths());
        assertEquals(LoanStatus.APPROVED, firstLoan.getStatus());
        
        // Verify second loan
        Loan secondLoan = result.get(1);
        assertEquals("loan-2", secondLoan.getId());
        assertEquals("1", secondLoan.getUserId());
        assertEquals(new BigDecimal("10000.00"), secondLoan.getAmount());
        assertEquals(4.5, secondLoan.getInterestRate());
        assertEquals(24, secondLoan.getDurationInMonths());
        assertEquals(LoanStatus.PENDING, secondLoan.getStatus());

        verify(jpaLoanRepository, times(1)).findByUser_IdOrderByStartDateDesc(userId);
    }

    @Test
    void findByUserId_shouldReturnEmptyListWhenNoLoansFound() {
        // Given
        String userId = "999L";
        when(jpaLoanRepository.findByUser_IdOrderByStartDateDesc(userId)).thenReturn(Arrays.asList());

        // When
        List<Loan> result = loanRepositoryUseCaseAdapter.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaLoanRepository, times(1)).findByUser_IdOrderByStartDateDesc(userId);
    }

    @Test
    void findByUserId_shouldHandleSingleLoanResult() {
        // Given
        String userId = "1L";
        List<LoanEntity> loanEntities = Arrays.asList(loanEntity1);
        when(jpaLoanRepository.findByUser_IdOrderByStartDateDesc(userId)).thenReturn(loanEntities);

        // When
        List<Loan> result = loanRepositoryUseCaseAdapter.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        Loan loan = result.get(0);
        assertEquals("loan-1", loan.getId());
        assertEquals("1", loan.getUserId());
        assertEquals(new BigDecimal("5000.00"), loan.getAmount());
        assertEquals(LoanStatus.APPROVED, loan.getStatus());

        verify(jpaLoanRepository, times(1)).findByUser_IdOrderByStartDateDesc(userId);
    }
}