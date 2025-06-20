package org.example.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.model.enums.ApplicationStatus;

import java.math.BigDecimal;

@Setter
@Getter
public class LoanApplication {
    private String id;
    private String userId;
    private ApplicationStatus status;
    private BigDecimal amount;

}
