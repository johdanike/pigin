package org.example.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProfileData {
    private String userId;
    private BigDecimal monthlyWalletInflow;
    private int airtimeTopUpsPerMonth;
    private int utilityPaymentScore;
    private int finTechActivityScore;
    private int trainingProgramScore;
    private int totalScore;
}
