package org.example.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.model.enums.RepaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class Repayment {
    private String id;
    private String loanId;
    private LocalDate dueDate;
    private BigDecimal amountDue;
    private RepaymentStatus status;
}
