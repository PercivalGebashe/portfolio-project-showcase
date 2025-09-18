package io.github.PercivalGebashe.portfolio_project_showcase.repository;

import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
