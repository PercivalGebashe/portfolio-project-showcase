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
import java.util.Set;
import java.util.stream.Collectors;

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
    public void updateExperience(Integer userId, ExperienceUpdateDTO experienceUpdateDTO) {
        UserAccount userAccount = userAccountService.findByUserId(userId);
        List<ExperienceDTO> dtos = experienceUpdateDTO.toDto();

        // Separate DTOs into existing and new
        List<ExperienceDTO> existingDtos = dtos.stream()
                .filter(dto -> dto.getExperienceId() != null)
                .toList();
        List<ExperienceDTO> newDtos = dtos.stream()
                .filter(dto -> dto.getExperienceId() == null)
                .toList();

        // Update existing experiences
        existingDtos.forEach(dto -> {
            Experience exp = experienceRepository.findById(dto.getExperienceId())
                    .orElseThrow(() -> new IllegalStateException("Experience not found: " + dto.getExperienceId()));
            exp.setCompanyName(dto.getCompany());
            exp.setPosition(dto.getPosition());
            exp.setDescription(dto.getDescription());
            exp.setTechnologies(dto.getTechnologies());
            exp.setStartDate(dto.getStartDate());
            exp.setEndDate(dto.getEndDate());
            exp.setCurrent(dto.isCurrent());
        });

        List<Experience> newExperiences = newDtos.stream()
                .map(dto -> {
                    Experience exp = new Experience();
                    exp.setUserAccount(userAccount);
                    exp.setCompanyName(dto.getCompany());
                    exp.setPosition(dto.getPosition());
                    exp.setDescription(dto.getDescription());
                    exp.setTechnologies(dto.getTechnologies());
                    exp.setStartDate(dto.getStartDate());
                    exp.setEndDate(dto.getEndDate());
                    exp.setCurrent(dto.isCurrent());
                    return exp;
                })
                .toList();

        System.out.println("EXP TO SAVE: " + newExperiences);

        List<Experience> savedExperiences = experienceRepository.saveAllAndFlush(newExperiences);

        System.out.println("SAVED EXP: " + savedExperiences);
    }


    public UserAccount findUserById(Integer userId) {
        return userAccountService.findByUserId(userId);
    }
}
