package org.example.application.port.output;

import org.example.domain.model.User;

import java.util.Optional;

public interface UserRepositoryUseCase {
    Optional<User> findById(String id);
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String username);
}
