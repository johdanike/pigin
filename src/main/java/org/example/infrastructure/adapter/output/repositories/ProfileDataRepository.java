package org.example.infrastructure.adapter.output.repositories;

import org.example.domain.model.ProfileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDataRepository extends JpaRepository<ProfileData, Long> {

}
