package org.example.domain.service;

import org.example.domain.model.Loan;
import org.example.domain.model.User;
import org.example.application.port.input.CreateNewLoanUseCase;
import org.example.application.port.output.CreditScoringUseCase;
import org.example.application.port.output.LoanRepositoryUseCase;
import org.example.application.port.output.UserRepositoryUseCase;
import org.example.domain.model.enums.LoanStatus;
import org.example.infrastructure.adapter.input.dto.requests.LoanRequestDTO;
import org.example.infrastructure.adapter.input.dto.requests.RepaymentDTO;
import org.example.infrastructure.adapter.input.dto.responses.AdminLoanSummaryDto;
import org.example.infrastructure.adapter.input.dto.responses.DefaultedLoanDto;
import org.example.infrastructure.adapter.input.dto.responses.LoanApplicationResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.LoanStatusResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("scoringService")
public class LoanUseCaseApplicationService implements CreateNewLoanUseCase {

    private final LoanRepositoryUseCase loanRepositoryUseCase;
    private final CreditScoringUseCase creditScoringUseCase;
    private final UserRepositoryUseCase userRepositoryUseCase;

    public LoanUseCaseApplicationService(LoanRepositoryUseCase loanRepositoryUseCase, CreditScoringUseCase creditScoringUseCase, UserRepositoryUseCase userRepositoryUseCase) {
        this.loanRepositoryUseCase = loanRepositoryUseCase;
        this.creditScoringUseCase = creditScoringUseCase;
        this.userRepositoryUseCase = userRepositoryUseCase;
    }

    @Override
    public Loan applyForLoan(String userId, BigDecimal amount, int durationInMonths) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Invalid amount");

        if (durationInMonths <= 0) throw new IllegalArgumentException("Invalid duration");

        User user = userRepositoryUseCase.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BigDecimal loanableAmount = creditScoringUseCase.getCreditScore(user);
        if (amount.compareTo(loanableAmount) > 0) throw new IllegalArgumentException("Requested amount exceeds limit");
        Loan loan = new Loan(userId, amount, 0.12, durationInMonths);
        return loanRepositoryUseCase.save(loan);
    }

    @Override
    public LoanApplicationResponseDto createLoan(LoanRequestDTO loanRequest) {
        // Validate loan request
        if (loanRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid loan amount");
        }
        if (loanRequest.getDurationInMonths() <= 0) {
            throw new IllegalArgumentException("Invalid loan duration");
        }

        // Fetch user and check credit score
        User user = userRepositoryUseCase.findById(loanRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BigDecimal loanableAmount = creditScoringUseCase.getCreditScore(user);
        if (loanRequest.getAmount().compareTo(loanableAmount) > 0) {
            throw new IllegalArgumentException("Requested amount exceeds limit");
        }

        // Create and save loan
        Loan loan = new Loan(loanRequest.getUserId(), loanRequest.getAmount(), 0.12, loanRequest.getDurationInMonths());
        loanRepositoryUseCase.save(loan);

        // Return response
        return new LoanApplicationResponseDto(loan.getId(), loan.getUserId(), loan.getAmount().doubleValue(), loan.getAmount().doubleValue(), loan.getStatus().name(), null, null, loan.getStartDate().atStartOfDay(), loan.getEndDate().atStartOfDay(), loan.getInterestRate(), "Loan created successfully");
    }

    @Override
    public LoanStatusResponseDto getLoanStatus(String loanId) {
        Loan loan = loanRepositoryUseCase.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        return new LoanStatusResponseDto(loan.getId(), loan.getUserId(), loan.getStatus().name(), loan.getAmount(), loan.getRemainingAmount(), null, loan.getEndDate().atStartOfDay(), null, null, null, false, null, null);
    }

    @Override
    public List<Loan> getLoansByUserId(String userId) {
        return loanRepositoryUseCase.findByUserId(userId);
    }

    @Override
    public RepaymentDTO processRepayment(String loanId, Double amount) {
        Loan loan = loanRepositoryUseCase.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        // Process repayment logic here
        // Update loan status or balance
        return new RepaymentDTO();
    }

    @Override
    public List<AdminLoanSummaryDto> getAllLoansForAdmin(String status, int page, int size) {
        // Fetch and return loans based on status and pagination
        return loanRepositoryUseCase.findByStatus(LoanStatus.valueOf(status))
                .stream()
                .map(loan -> {
                        AdminLoanSummaryDto dto = new AdminLoanSummaryDto();
                        dto.setLoanId(loan.getId());
                        // Placeholder for penalty amount
                        return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DefaultedLoanDto> getDefaultedLoans() {
        // Fetch and return defaulted loans
        return loanRepositoryUseCase.findOverdueLoans()
                .stream()
                .map(loan -> {
                    User user = userRepositoryUseCase.findById(loan.getUserId())
                            .orElse(null);
                    
                    return DefaultedLoanDto.builder()
                            .loanId(loan.getId())
                            .userId(loan.getUserId())
                            .userEmail(user != null ? user.getEmail() : null)
                            .userName(user != null ? user.getFullName() : null)
                            .userPhone(user != null ? user.getPhone() : null)
                            .originalAmount(loan.getAmount().doubleValue())
                            .remainingBalance(loan.getRemainingAmount().doubleValue())
                            .totalRepaid(loan.getAmount().subtract(loan.getRemainingAmount()).doubleValue())
                            .dueDate(loan.getEndDate().atStartOfDay())
                            .defaultDate(loan.getEndDate().atStartOfDay()) // You might want to use actual default date
                            .daysPastDue(calculateDaysPastDue(loan))
                            .penaltyAmount(calculatePenaltyAmount(loan))
                            .recoveryStatus(loan.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Object updateLoanStatus(String loanId, String status, String reason) {
        Loan loan = loanRepositoryUseCase.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setStatus(LoanStatus.valueOf(status));
        loanRepositoryUseCase.save(loan);
        return "Loan status updated";
    }

    @Override
    public Object getAdminDashboardSummary() {
        // Implement logic to summarize loan data for admin dashboard
        return "Dashboard summary";
    }

    private Integer calculateDaysPastDue(Loan loan) {
        // Calculate days past due based on end date
        if (loan.getEndDate() != null && loan.getEndDate().isBefore(LocalDate.now())) {
            return (int) ChronoUnit.DAYS.between(loan.getEndDate(), LocalDate.now());
        }
        return 0;
    }

    private Double calculatePenaltyAmount(Loan loan) {
        // Calculate penalty amount based on days past due
        Integer daysPastDue = calculateDaysPastDue(loan);
        if (daysPastDue > 0) {
            // Example: 1% penalty per day past due
            return loan.getRemainingAmount().doubleValue() * 0.01 * daysPastDue;
        }
        return 0.0;
    }

}
