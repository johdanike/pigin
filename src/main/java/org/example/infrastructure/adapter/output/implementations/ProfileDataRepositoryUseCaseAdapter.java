package org.example.infrastructure.adapter.output.implementations;

import org.example.application.port.output.ProfileDataRepositoryUseCase;
import org.example.domain.model.ProfileData;
import org.example.infrastructure.adapter.output.repositories.ProfileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileDataRepositoryUseCaseAdapter implements ProfileDataRepositoryUseCase {

    @Autowired
    private ProfileDataRepository profileDataRepository;

    @Override
    public ProfileData save(ProfileData profileData) {
        // This would need proper mapping between ProfileData and ProfileDataEntity
        // For now, returning the input as a placeholder
        return profileData;
    }

    @Override
    public Optional<ProfileData> findByUserId(Long userId) {
        Optional<Object> result = profileDataRepository.findByUserId(String.valueOf(userId));
        // This would need proper mapping from ProfileDataEntity to ProfileData
        // For now, returning empty as a placeholder
        return Optional.empty();
    }
}