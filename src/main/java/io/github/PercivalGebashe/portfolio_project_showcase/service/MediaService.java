package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProfileRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.UserAccountRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MediaService {

    @Value("${imageUploadUrl}")
    private String baseUrl;

    private final ProfileRepository profileRepository;
    private final UserAccountRepository userAccountRepository;

    public MediaService(ProfileRepository profileRepository, UserAccountRepository userAccountRepository) {
        this.profileRepository = profileRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public String changeImageUrl(Integer useId, MultipartFile multipartFile){
        Optional<Profile> profileOptional = profileRepository.findByUserAccount_UserId(useId);

        if(profileOptional.isPresent()){
            Profile profile = profileOptional.get();

            profile.setProfilePictureUrl(baseUrl);
        }
        return "";
    }

    @Async
    private CompletableFuture<String> UploadImage(MultipartFile multipartFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(bufferedImage)
                    .size(1200, 800)
                    .outputQuality(0.85f)
                    .toOutputStream(outputStream);

            byte[] imageBytes = outputStream.toByteArray();

            String url = upLoadToRemote(imageBytes, multipartFile.getOriginalFilename());

            return CompletableFuture.completedFuture(url);
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    private String upLoadToRemote(byte[] imageBytes, String originalFilename) {

        return null;
    }
}
