package org.example.infrastructure.adapter.input.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UploadProfileRequestDto {
    private BigDecimal walletInflow;
    private int airtimeTopUpsPerMonth;
    private int utilityPayments;
    private int fintechActivity;
    private int trainingPrograms;
}
