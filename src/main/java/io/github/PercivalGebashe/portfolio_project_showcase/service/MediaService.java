package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.model.Profile;
import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProfileRepository;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MediaService {

    @Value("${imageKit.url-endpoint}")
    private String urlEndpoint;

    @Value("${imageKit.private-key}")
    private String privateKey;

    @Value("${imageKit.public-key}")
    private String publicKey;

    private final ProfileRepository profileRepository;
    private final ImageKit imageKit = ImageKit.getInstance();

    @Autowired
    public MediaService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @PostConstruct
    public void init() {
        Configuration configuration = new Configuration(publicKey, privateKey, urlEndpoint);
        imageKit.setConfig(configuration);
        System.out.println("ImageKit config initialized");
    }

    public CompletableFuture<Void> changeImageUrl(Integer userId, MultipartFile multipartFile) {
        Optional<Profile> profileOptional = profileRepository.findByUserAccount_UserId(userId);

        if (profileOptional.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Profile not found for user ID: " + userId));
        }

        Profile profile = profileOptional.get();

        return uploadProfileImage(userId, multipartFile)
                .thenAccept(url -> {
                    if (url != null) {
                        profile.setProfilePictureUrl(url);
                        profileRepository.save(profile);
                    } else {
                        throw new RuntimeException("Failed to upload image for user ID: " + userId);
                    }
                });
    }

    @Async
    public CompletableFuture<List<String>> upLoadImages(Integer projectId, List<MultipartFile> multipartFiles) {
        try {
            // Convert files to BufferedImages
            List<BufferedImage> bufferedImages = multipartFiles.stream()
                .map(part -> {
                    try {
                        return ImageIO.read(part.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();

            if (bufferedImages.isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }

            // Process and upload each image concurrently
            List<CompletableFuture<String>> uploadFutures = new ArrayList<>();

            for (int i = 0; i < bufferedImages.size(); i++) {
                BufferedImage img = bufferedImages.get(i);
                String fileName = projectId + "_project_image_" + i;

                uploadFutures.add(
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            // Convert to compressed JPG
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            Thumbnails.of(img)
                                    .size(1200, 800)
                                    .outputQuality(0.85f)
                                    .outputFormat("jpg")
                                    .toOutputStream(outputStream);

                            byte[] imageBytes = outputStream.toByteArray();

                            return uploadImageToRemote(imageBytes, fileName);
                        } catch (Exception e) {
                            throw new RuntimeException("Error uploading image " + fileName, e);
                        }
                    })
                );
            }

            // Combine all async results into one list
            CompletableFuture<Void> allUploads = CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]));

            return allUploads.thenApply(v ->
                uploadFutures.stream()
                    .map(CompletableFuture::join)
                    .toList()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    public CompletableFuture<String> uploadProfileImage(Integer userId, MultipartFile multipartFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

            if (bufferedImage == null) {
                return CompletableFuture.completedFuture(null);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(bufferedImage)
                    .size(1200, 800)
                    .outputQuality(0.85f)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] imageBytes = outputStream.toByteArray();

            String url = uploadImageToRemote(imageBytes, (userId.toString() + "_profile_image"));
            return CompletableFuture.completedFuture(url);

        } catch (IOException | ForbiddenException | TooManyRequestsException | InternalServerException |
                 UnauthorizedException | BadRequestException | UnknownException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    private String uploadImageToRemote(byte[] imageBytes, String fileName) throws ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        FileCreateRequest fileCreateRequest = new FileCreateRequest(imageBytes, fileName);
        fileCreateRequest.setFolder("/projectshowcase/images/");
        fileCreateRequest.setUseUniqueFileName(true);
        Result result = imageKit.upload(fileCreateRequest);
        return result.getUrl();
    }

}