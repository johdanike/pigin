package org.example.domain.service;

import org.example.application.port.output.CreditScoringUseCase;
import org.example.domain.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ScoringService implements CreditScoringUseCase {

    private static final BigDecimal CREDIT_SCORE = new BigDecimal(50);
    private static final BigDecimal MINIMUM_CREDIT_SCORE = new BigDecimal(50);
    private static final BigDecimal MAXIMUM_CREDIT_SCORE = new BigDecimal(100);
    private static final BigDecimal LOW_RISK_THRESHOLD = new BigDecimal(70);


    @Override
    public BigDecimal getCreditScore(User user) {
        return null;
    }
}
