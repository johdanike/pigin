package org.example.infrastructure.adapter.output.implementations;

import org.example.infrastructure.adapter.output.entity.LoanEntity;
import org.example.infrastructure.adapter.output.entity.UserEntity;
import org.example.infrastructure.adapter.output.repositories.JpaLoanRepository;
import org.example.infrastructure.adapter.output.repositories.JpaUserRepository;
import org.example.domain.model.Loan;
import org.example.domain.model.enums.LoanStatus;
import org.example.application.port.output.LoanRepositoryUseCase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanRepositoryUseCaseAdapter implements LoanRepositoryUseCase {

    private final JpaLoanRepository jpaLoanRepository;
    private final JpaUserRepository jpaUserRepository;

    public LoanRepositoryUseCaseAdapter(JpaLoanRepository jpaLoanRepository, JpaUserRepository jpaUserRepository) {
        this.jpaLoanRepository = jpaLoanRepository;
        this.jpaUserRepository = jpaUserRepository;
    }


    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = new LoanEntity();
        if (loan.getId() != null) {
            entity.setId(loan.getId());
        }
        
        // Set user relationship
        UserEntity userEntity = jpaUserRepository.findById(loan.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        entity.setUser(userEntity);
        
        entity.setAmount(loan.getAmount());
        entity.setInterestRate(loan.getInterestRate());
        entity.setDurationInMonths(loan.getDurationInMonths());
        entity.setStartDate(loan.getStartDate());
        entity.setEndDate(loan.getEndDate());
        entity.setStatus(loan.getStatus().name());
        
        LoanEntity savedEntity = jpaLoanRepository.save(entity);
        loan.setId(savedEntity.getId());
        return loan;
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        List<LoanEntity> loanEntities = jpaLoanRepository.findByUser_IdOrderByStartDateDesc(userId);
        return loanEntities.stream()
                .map(this::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> findById(String id) {
        Optional<LoanEntity> entityOpt = jpaLoanRepository.findById(id);
        return entityOpt.map(this::mapToDomainModel);
    }

    @Override
    public List<Loan> findAll() {
        List<LoanEntity> entities = jpaLoanRepository.findAll();
        return entities.stream()
                .map(this::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        List<LoanEntity> entities = jpaLoanRepository.findByStatus(status.name());
        return entities.stream()
                .map(this::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findOverdueLoans() {
        // Implementation for finding overdue loans
        // This would typically involve checking dates and status
        return jpaLoanRepository.findAll().stream()
                .map(this::mapToDomainModel)
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    private Loan mapToDomainModel(LoanEntity entity) {
        Loan loan = new Loan();
        loan.setId(entity.getId());
        loan.setUserId(entity.getUser().getId()); // Get user ID from UserEntity
        loan.setAmount(entity.getAmount());
        loan.setInterestRate(entity.getInterestRate());
        loan.setDurationInMonths(entity.getDurationInMonths());
        loan.setStartDate(entity.getStartDate());
        loan.setEndDate(entity.getEndDate());
        loan.setStatus(LoanStatus.valueOf(entity.getStatus()));
        return loan;
    }
}
