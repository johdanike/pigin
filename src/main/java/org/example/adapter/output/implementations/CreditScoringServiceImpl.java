package org.example.adapter.output.implementations;

import org.example.domain.model.User;
import org.example.domain.port.output.CreditScoringService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CreditScoringServiceImpl implements CreditScoringService {
    @Override
    public BigDecimal getCreditScore(User user) {
        return null;
    }
}
