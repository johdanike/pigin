package org.example.infrastructure.adapter.input.dto.requests;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
public class WalletInflow {
    private BigDecimal monthlySalary;
    private BigDecimal businessRevenue;
    private BigDecimal remittances;
}
