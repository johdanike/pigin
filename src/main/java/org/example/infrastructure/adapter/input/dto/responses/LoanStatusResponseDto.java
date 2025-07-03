package org.example.infrastructure.adapter.input.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusResponseDto {
    private String loanId;
    private String userId;
    private String status;
    private BigDecimal originalAmount;
    private BigDecimal remainingBalance;
    private BigDecimal totalRepaid;
    private LocalDateTime dueDate;
    private LocalDateTime lastPaymentDate;
    private BigDecimal weeklyPaymentAmount;
    private Integer weeksRemaining;
    private Boolean overdue;
    private Integer daysPastDue;
    private BigDecimal penaltyAmount;
}