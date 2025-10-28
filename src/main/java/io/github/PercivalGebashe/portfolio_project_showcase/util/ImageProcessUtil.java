package io.github.PercivalGebashe.portfolio_project_showcase.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageProcessUtil {

    public static Map<String,byte[]> processImage(MultipartFile multipartFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(bufferedImage)
                    .size(1200, 800)
                    .outputQuality(0.85f)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            Thumbnails.of(image)
                    .size(1200, 800)
                    .outputQuality(0.85f)
                    .outputFormat("jpg")
                    .toOutputStream(imageOutputStream);

            return Map.of(multipartFile.getOriginalFilename(), imageOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, byte[]>> processImages(List<MultipartFile> files) {

        List<Map<String, byte[]>> imageBytes = new ArrayList<>();

        files.forEach(file -> imageBytes.add(processImage(file)));

        return imageBytes;
    }
}
