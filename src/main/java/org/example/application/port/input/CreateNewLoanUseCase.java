package org.example.application.port.input;

import org.example.domain.model.Loan;
import org.example.infrastructure.adapter.input.dto.requests.LoanRequestDTO;
import org.example.infrastructure.adapter.input.dto.requests.RepaymentDTO;
import org.example.infrastructure.adapter.input.dto.responses.AdminLoanSummaryDto;
import org.example.infrastructure.adapter.input.dto.responses.DefaultedLoanDto;
import org.example.infrastructure.adapter.input.dto.responses.LoanApplicationResponseDto;
import org.example.infrastructure.adapter.input.dto.responses.LoanStatusResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreateNewLoanUseCase {
    Loan applyForLoan(String userId, BigDecimal amount, int monthDuration);

    LoanApplicationResponseDto createLoan(LoanRequestDTO loanRequest);

    LoanStatusResponseDto getLoanStatus(String loanId);

    List<Loan> getLoansByUserId(String userId);

    RepaymentDTO processRepayment(String loanId, Double amount);

    List<AdminLoanSummaryDto> getAllLoansForAdmin(String status, int page, int size);

    List<DefaultedLoanDto> getDefaultedLoans();

    Object updateLoanStatus(String loanId, String status, String reason);

    Object getAdminDashboardSummary();
}
