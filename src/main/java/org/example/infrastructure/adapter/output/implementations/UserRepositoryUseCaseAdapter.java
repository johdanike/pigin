package org.example.infrastructure.adapter.output.implementations;

import org.example.infrastructure.adapter.output.entity.UserEntity;
import org.example.infrastructure.adapter.output.repositories.JpaUserRepository;
import org.example.domain.model.User;
import org.example.application.port.output.UserRepositoryUseCase;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryUseCaseAdapter implements UserRepositoryUseCase {

    private final JpaUserRepository userRepository;

    public UserRepositoryUseCaseAdapter(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity entity = toEntity(user);
        userRepository.save(entity);
    }

    @Override
    public Object findbyEmail(String mail) {
        return userRepository.findByEmail(mail);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUserName(),
                entity.getEmail()
        );
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        return entity;
    }
}
