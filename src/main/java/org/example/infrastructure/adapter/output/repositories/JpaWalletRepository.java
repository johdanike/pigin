package org.example.infrastructure.adapter.output.repositories;
import org.example.application.port.output.WalletRepositoryUseCase;
import org.example.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaWalletRepository extends JpaRepository<Wallet, Long>, WalletRepositoryUseCase {
    Optional<Wallet> findByUserId(Long userId);
}