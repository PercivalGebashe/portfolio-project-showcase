package io.github.PercivalGebashe.portfolio_project_showcase.repository;

import io.github.PercivalGebashe.portfolio_project_showcase.model.EmailValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailValidationStatusRepository extends JpaRepository<EmailValidationStatus, Integer> {
}
