package io.github.PercivalGebashe.portfolio_project_showcase.service;

import io.github.PercivalGebashe.portfolio_project_showcase.repository.ProfileRepository;
import io.github.PercivalGebashe.portfolio_project_showcase.util.ImageProcessUtil;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Async
    public CompletableFuture<List<String>> upLoadImages(List<MultipartFile> multipartFiles) {
        try {

            List<Map<String, byte[]>> imageDataList = ImageProcessUtil.processImages(multipartFiles);
            List<CompletableFuture<String>> uploadFutures = new ArrayList<>();
            if (!imageDataList.isEmpty()) {
                for (Map<String, byte[]> data : imageDataList) {
                    uploadFutures.add(
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                return uploadImageToRemote(data);
                            } catch (ForbiddenException e) {
                                throw new RuntimeException(e);
                            } catch (TooManyRequestsException e) {
                                throw new RuntimeException(e);
                            } catch (InternalServerException e) {
                                throw new RuntimeException(e);
                            } catch (UnauthorizedException e) {
                                throw new RuntimeException(e);
                            } catch (BadRequestException e) {
                                throw new RuntimeException(e);
                            } catch (UnknownException e) {
                                throw new RuntimeException(e);
                            }
                        })
                    );
                }
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
    public CompletableFuture<String> uploadImage(MultipartFile multipartFile) {

        Map<String, byte[]> imageData = ImageProcessUtil.processImage(multipartFile);

        try {

            return CompletableFuture.completedFuture(uploadImageToRemote(imageData));

        } catch (ForbiddenException e) {
            throw new RuntimeException(e);
        } catch (TooManyRequestsException e) {
            throw new RuntimeException(e);
        } catch (InternalServerException e) {
            throw new RuntimeException(e);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (UnknownException e) {
            throw new RuntimeException(e);
        }
    }

    private String uploadImageToRemote(Map<String, byte[]> imageData) throws ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        String fileName = imageData.keySet().stream().findFirst().get();
        byte[] imageBytes = imageData.get(fileName);
        FileCreateRequest fileCreateRequest = new FileCreateRequest(imageBytes, fileName);
        fileCreateRequest.setFolder("/projectshowcase/images/");
        fileCreateRequest.setUseUniqueFileName(true);
        Result result = imageKit.upload(fileCreateRequest);
        return result.getUrl();
    }

}