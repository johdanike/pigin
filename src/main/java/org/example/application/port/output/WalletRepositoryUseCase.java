package org.example.application.port.output;
import org.example.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepositoryUseCase {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByUserId(Long userId);
}
