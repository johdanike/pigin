package org.example.application.port.output;

import org.example.domain.model.User;

import java.math.BigDecimal;

public interface CreditScoringUseCase {
    BigDecimal getCreditScore(User user);
}
