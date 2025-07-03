package org.example.application.port.output;

import org.example.domain.model.ProfileData;

import java.util.Optional;

public interface ProfileDataRepositoryUseCase {
    ProfileData save(ProfileData profileData);
    Optional<ProfileData> findByUserId(Long userId);
}
