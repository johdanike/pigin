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
public class AdminLoanSummaryDto {
    private String loanId;
    private String userId;
    private String userEmail;
    private String userName;
    private Double loanAmount;
    private String status;
    private Integer creditScore;
    private LocalDateTime applicationDate;
    private LocalDateTime dueDate;
    private Double remainingBalance;
    private Boolean isOverdue;
    private Integer daysPastDue;
}