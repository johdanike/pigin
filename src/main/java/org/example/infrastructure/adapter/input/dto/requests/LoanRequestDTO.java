package org.example.infrastructure.adapter.input.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {
    private String userId;
    private BigDecimal amount;
    private Integer durationInMonths;
}
