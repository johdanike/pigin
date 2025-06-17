package org.example.infrastructure.adapter.output.implementations;

import org.example.domain.model.User;
import org.example.application.port.output.CreditScoringUseCase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditScoringUseCaseImpl implements CreditScoringUseCase {
    @Override
    public BigDecimal getCreditScore(User user) {
        return null;
    }
}
