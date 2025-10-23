package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProfileUpdateDTO;
import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MediaService mediaService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, MediaService mediaService) {
        this.profileRepository = profileRepository;
        this.mediaService = mediaService;
    }

    public Profile findByUserId(Integer userId){
        return profileRepository.findByUserAccount_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));
    }

    @Transactional
    public void updateProfile(Integer userId, ProfileUpdateDTO requestDto) {
        Profile profile = this.findByUserId(userId);

        profile.setFirstName(requestDto.getFirstName());
        profile.setLastName(requestDto.getLastName());
        profile.setTagline(requestDto.getTagline());
        profile.setBio(requestDto.getBio());
        profile.setProfilePictureUrl(requestDto.getProfilePictureUrl());
        profile.setSkills(requestDto.getSkills());
        profile.setContactLinks(requestDto.getContactLinks());

        profileRepository.save(profile);
    }
}