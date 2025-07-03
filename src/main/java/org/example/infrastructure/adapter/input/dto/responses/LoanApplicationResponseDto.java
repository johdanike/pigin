package org.example.infrastructure.adapter.input.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponseDto {
    private String loanId;
    private String userId;
    private Double requestedAmount;
    private Double approvedAmount;
    private String status;
    private Integer creditScore;
    private String rejectionReason;
    private LocalDateTime applicationDate;
    private LocalDateTime dueDate;
    private Double interestRate;
    private String message;
}