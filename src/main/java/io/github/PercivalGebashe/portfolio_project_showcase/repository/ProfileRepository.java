package io.github.PercivalGebashe.portfolio_project_showcase.repository;

import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    Optional<Profile> findByUserAccount_UserId(Integer userId);
}
