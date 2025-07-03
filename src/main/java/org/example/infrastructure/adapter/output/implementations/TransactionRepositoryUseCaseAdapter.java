package org.example.infrastructure.adapter.output.implementations;

import org.example.application.port.output.TransactionRepositoryUseCase;
import org.example.domain.model.Transaction;
import org.example.infrastructure.adapter.output.repositories.JpaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionRepositoryUseCaseAdapter implements TransactionRepositoryUseCase {

    @Autowired
    private JpaTransactionRepository jpaTransactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return jpaTransactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return jpaTransactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return jpaTransactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> findByLoanId(Long loanId) {
        return jpaTransactionRepository.findByLoanId(loanId);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaTransactionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaTransactionRepository.deleteById(id);
    }
}