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
public class DefaultedLoanDto {
    private String loanId;
    private String userId;
    private String userEmail;
    private String userName;
    private String userPhone;
    private Double originalAmount;
    private Double remainingBalance;
    private Double totalRepaid;
    private LocalDateTime dueDate;
    private LocalDateTime defaultDate;
    private Integer daysPastDue;
    private Double penaltyAmount;
    private String lastContactDate;
    private String recoveryStatus;
}