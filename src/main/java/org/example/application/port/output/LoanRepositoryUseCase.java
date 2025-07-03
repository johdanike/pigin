package org.example.application.port.output;

import org.example.domain.model.Loan;
import org.example.domain.model.enums.LoanStatus;

import java.util.List;
import java.util.Optional;

public interface LoanRepositoryUseCase {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findByUserId(String userId);
    List<Loan> findAll();
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findOverdueLoans();
}
