package org.example.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.example.domain.model.enums.LoanStatus;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private String id;
    private String userId;
    private BigDecimal amount;
    private double interestRate;
    private int durationInMonths;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status;

    public Loan(String userId, BigDecimal amount, double interestRate, int durationInMonths) {
        this.userId = userId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
        this.startDate = LocalDate.now();
        this.endDate = this.startDate.plusMonths(durationInMonths);
        this.status = LoanStatus.PENDING;
    }

    public Loan(String userId, BigDecimal amount, LocalDateTime dueDate) {
        this.userId = userId;
        this.amount = amount;
        this.startDate = LocalDate.from(dueDate);
    }


    public void checkEligibility() {

    }
    public void processPayment() {

    }

    public void setCreditScore(int creditScore) {
        if (creditScore >= 700) {
            this.status = LoanStatus.APPROVED;
        } else if (creditScore > 600) {
            this.status = LoanStatus.PENDING_REVIEW;
        } else {
            this.status = LoanStatus.REJECT;
        }
    }

    // Additional methods needed by the application
    @Setter
    private BigDecimal remainingAmount;

    public BigDecimal getRemainingAmount() {
        return remainingAmount != null ? remainingAmount : amount;
    }

}

