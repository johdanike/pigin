package org.example.infrastructure.adapter.output.implementations;

import org.example.domain.model.User;
import org.example.infrastructure.adapter.input.dto.responses.SignUpResponseDto;
import org.example.infrastructure.adapter.output.entity.UserEntity;
import org.example.infrastructure.adapter.output.repositories.JpaUserRepository;
import org.example.application.port.output.UserRepositoryUseCase;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryUseCaseAdapter implements UserRepositoryUseCase {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryUseCaseAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        try {
            return jpaUserRepository.findById(id).map(this::toDomain);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return jpaUserRepository.findByUserName(username).map(this::toDomain);
    }


//    @Override
//    public User save(User user) {
//        UserEntity userEntity = user.getId() != null
//                ? jpaUserRepository.findById(user.getId())
//                .map(foundEntity -> updateEntityFromDomain(foundEntity, user))
//                .orElse(toEntity(user))
//                :toEntity(user);
//        return toDomain(jpaUserRepository.save(toEntity(user)));
//    }

    @Override
    public User save(User user) {
        UserEntity entity;

        if (user.getId() != null) {
            entity = jpaUserRepository.findById(user.getId())
                    .map(existing -> updateEntityFromDomain(existing, user))
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));
        } else {
            entity = toEntity(user);
        }

        return toDomain(jpaUserRepository.save(entity));
    }


    private UserEntity updateEntityFromDomain(UserEntity entity, User user) {
        return getUserEntity(user, entity);
    }


    private User toDomain(UserEntity userEntity){
        User user = new User();
        user.setEmail(userEntity.getEmail());
        user.setUserName(userEntity.getUserName());
        user.setPassword(userEntity.getPassword());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setBvn(userEntity.getBvn());
        user.setEmail(userEntity.getEmail());
        user.setPhone(userEntity.getPhone());
        user.setId(userEntity.getId());
        return user;
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        return getUserEntity(user, entity);
    }

    private static UserEntity getUserEntity(User user, UserEntity entity) {
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setUserName(user.getUserName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setIsLoggedIn(user.getIsLoggedIn());
        entity.setPhone(user.getPhone());
        entity.setId(user.getId());
        entity.setBvn(user.getBvn());
        entity.setProfilePictureUrl(user.getProfilePictureUrl());

        return entity;
    }
}
