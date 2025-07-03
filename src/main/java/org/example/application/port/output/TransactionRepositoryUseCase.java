package org.example.application.port.output;

import org.example.domain.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryUseCase {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByLoanId(Long loanId);
    List<Transaction> findAll();
    void deleteById(Long id);
}