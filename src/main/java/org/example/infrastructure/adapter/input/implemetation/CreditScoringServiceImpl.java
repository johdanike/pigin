package org.example.infrastructure.adapter.input.implemetation;

import org.example.domain.model.ProfileData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CreditScoringServiceImpl {

        private static final BigDecimal WALLET_THRESHOLD = new BigDecimal("30000");
        private static final int AIRTIME_THRESHOLD = 4;

        public int calculateCreditScore(ProfileData profileData) {
            int score = 0;

            // Wallet inflow scoring (₦30k+ = +30 points)
            if (profileData.getMonthlyWalletInflow().compareTo(WALLET_THRESHOLD) >= 0) {
                score += 30;
            } else {
                // Partial scoring for lower amounts
                double ratio = profileData.getMonthlyWalletInflow().divide(WALLET_THRESHOLD, 2, RoundingMode.HALF_UP).doubleValue();
                score += (int) (30 * ratio);
            }

            // Airtime topups scoring (4/month = +20 points)
            if (profileData.getAirtimeTopUpsPerMonth() >= AIRTIME_THRESHOLD) {
                score += 20;
            } else {
                score += (profileData.getAirtimeTopUpsPerMonth() * 20) / AIRTIME_THRESHOLD;
            }

            // Utility payment score (max +20 points)
            score += Math.min(profileData.getUtilityPaymentScore(), 20);

            // Fintech activity score (max +15 points)
            score += Math.min(profileData.getFinTechActivityScore(), 15);

            // Training program score (max +15 points)
            score += Math.min(profileData.getTrainingProgramScore(), 15);

            return Math.min(score, 100); // Cap at 100
        }

        public LoanDecision makeLoanDecision(int creditScore) {
            if (creditScore >= 60) {
                return LoanDecision.APPROVE;
            } else if (creditScore >= 40) {
                return LoanDecision.PENDING_REVIEW;
            } else {
                return LoanDecision.REJECT;
            }
        }

        public enum LoanDecision {
            APPROVE, PENDING_REVIEW, REJECT
        }
    }
