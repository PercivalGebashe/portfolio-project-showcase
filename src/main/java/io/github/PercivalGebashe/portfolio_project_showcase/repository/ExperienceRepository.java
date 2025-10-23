package io.github.PercivalGebashe.portfolio_project_showcase.repository;

import io.github.PercivalGebashe.portfolio_project_showcase.model.Experience;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

    Optional<List<Experience>> findByUserAccount_UserId(Integer userId);

    List<Experience> findByUserAccount(UserAccount userAccount);
}
