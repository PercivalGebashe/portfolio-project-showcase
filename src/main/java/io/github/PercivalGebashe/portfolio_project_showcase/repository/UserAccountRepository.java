package io.github.PercivalGebashe.portfolio_project_showcase.repository;

import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByUserId(Integer userId);

    Optional<UserAccount> findByConfirmationToken(String confirmationToken);

    boolean existsByEmail(String email);
}
