package org.example.infrastructure.adapter.output.repositories;

import org.example.domain.model.ProfileData;
import org.example.infrastructure.adapter.output.entity.ProfileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileDataRepository extends JpaRepository<ProfileDataEntity, String> {

    Optional<Object> findByUserId(String userId);
}
