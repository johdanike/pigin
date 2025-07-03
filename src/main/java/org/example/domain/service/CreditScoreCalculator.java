package org.example.domain.service;

import org.example.infrastructure.adapter.input.dto.requests.WalletInflow;
import org.example.infrastructure.adapter.output.entity.ProfileDataEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class CreditScoreCalculator {

    private static final HashMap<BigDecimal, Integer> INFLOW_SCORE_MAP = new HashMap<>();

    static {
        INFLOW_SCORE_MAP.put(new BigDecimal("5000"), 5);
        INFLOW_SCORE_MAP.put(new BigDecimal("10000"), 10);
        INFLOW_SCORE_MAP.put(new BigDecimal("20000"), 15);
        INFLOW_SCORE_MAP.put(new BigDecimal("50000"), 20);
    }

    private static final int MAX_AIRTIME_SCORE = 20;
    private static final int AIRTIME_MULTIPLIER = 5;

    public int calculateTotalScore(ProfileDataEntity profileData) {
        int walletInflowScore = calculateWalletInflowScore(profileData);
        int airtimeScore = calculateAirtimeScore(profileData);
        int utilityScore = profileData.getUtilityPaymentScore();
        int fintechScore = profileData.getFinTechActivityScore();
        int trainingScore = profileData.getTrainingProgramScore();

        return walletInflowScore + airtimeScore + utilityScore + fintechScore + trainingScore;
    }

    private int calculateWalletInflowScore(ProfileDataEntity profileData) {
        WalletInflow inflow = profileData.getMonthlyWalletInflow();
        if (inflow == null) return 0;
        BigDecimal totalInflow = inflow.getMonthlySalary()
                .add(inflow.getBusinessRevenue())
                .add(inflow.getRemittances());

        int score = 0;
        for (BigDecimal threshold : INFLOW_SCORE_MAP.keySet()) {
            if (totalInflow.compareTo(threshold) >= 0) score = INFLOW_SCORE_MAP.get(threshold);
        }

        return score;
    }


    private int calculateAirtimeScore(ProfileDataEntity profileData) {
        int airtimeTopUps = profileData.getAirtimeTopUpsPerMonth();
        return Math.min(airtimeTopUps * AIRTIME_MULTIPLIER, MAX_AIRTIME_SCORE);
    }
}
