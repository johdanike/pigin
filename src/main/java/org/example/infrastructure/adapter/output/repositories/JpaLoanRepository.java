package org.example.infrastructure.adapter.output.repositories;

import org.example.infrastructure.adapter.output.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaLoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByUser_IdOrderByStartDateDesc(String userId);
    List<LoanEntity> findByStatus(String status);
}