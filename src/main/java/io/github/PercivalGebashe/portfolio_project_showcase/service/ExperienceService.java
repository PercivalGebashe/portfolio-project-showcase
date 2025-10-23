package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ExperienceDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.ExperienceUpdateDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Experience;
import io.github.PercivalGebashe.portfolio_project_showcase.model.UserAccount;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ExperienceRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;

    public ExperienceService(ExperienceRepository experienceRepository, UserAccountService userAccountService, UserAccountRepository userAccountRepository) {
        this.experienceRepository = experienceRepository;
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
    }

    public List<Experience> findByUserId(Integer userId){
        return experienceRepository.findByUserAccount_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found for userId: " + userId));
    }

    @Transactional
    public void updateExperience(Integer userId, ExperienceUpdateDTO experiences) {

        UserAccount userAccount = userAccountService.findByUserId(userId);
        List<ExperienceDTO> experienceDTOS = experiences.toDto();

        List<Experience> existingExperiences = new ArrayList<>(userAccount.getExperiences());

        List<Integer> incomingIds = experienceDTOS.stream()
                .map(ExperienceDTO::getExperienceId)
                .filter(Objects::nonNull)
                .toList();

        System.out.println("IDs: " + incomingIds);

        existingExperiences.removeIf(e ->
                e.getExperienceId() != null && !incomingIds.contains(e.getExperienceId())
        );

        userAccount.getExperiences().clear();
        for (ExperienceDTO dto : experienceDTOS) {
            Experience exp = dto.getExperienceId() != null
                    ? experienceRepository.findById(dto.getExperienceId()).orElse(new Experience())
                    : new Experience();

            exp.setUserAccount(userAccount);
            exp.setCompanyName(dto.getCompany());
            exp.setPosition(dto.getPosition());
            exp.setLocation(dto.getLocation());
            exp.setDescription(dto.getDescription());
            exp.setTechnologies(dto.getTechnologies());
            exp.setStartDate(dto.getStartDate());
            exp.setEndDate(dto.getEndDate());
            exp.setCurrent(dto.isCurrent());

            userAccount.getExperiences().add(exp);
        }

        userAccountRepository.save(userAccount);
    }

    public UserAccount findUserById(Integer userId) {
        return userAccountService.findByUserId(userId);
    }
}
