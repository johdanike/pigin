package org.example.infrastructure.adapter.input.dto.requests;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WalletInflow {
    private BigDecimal monthlySalary;
    private BigDecimal businessRevenue;
    private BigDecimal remittances;
}
