package com.augusto.backend.service;

import com.augusto.backend.service.exception.FileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class ImageService {

    private static final String PNG_FORMAT = "png";
    private static final String JPG_FORMAT = "jpg";
    private static final List<String> ACCEPTABLE_IMAGE_FORMATS = List.of(PNG_FORMAT, JPG_FORMAT);

    public Mono<BufferedImage> getJpgImageFromFile(FilePart filePart) {
        String fileExtension = FilenameUtils.getExtension(filePart.filename());

        if (!ACCEPTABLE_IMAGE_FORMATS.contains(fileExtension)) {
            return Mono.error(new FileException("Only the formats: " + ACCEPTABLE_IMAGE_FORMATS + " are permitted"));
        }

        return DataBufferUtils.join(filePart.content())
                .flatMap(inputStream -> readImage(inputStream.asInputStream()))
                .flatMap(image -> {
                    if (PNG_FORMAT.equals(fileExtension)) {
                        return pngToJpg(image);
                    } else {
                        return Mono.just(image);
                    }
                });
    }

    public Mono<ByteArrayInputStream> getInputStream(BufferedImage image, String extension) {
        return Mono.just(new ByteArrayOutputStream())
                .flatMap(os -> writeImage(image, extension, os)
                        .thenReturn(os))
                .map(os -> new ByteArrayInputStream(os.toByteArray()))
                .doOnError(e -> Mono.error(new FileException("Error on image processing.")));
    }

    private Mono<BufferedImage> pngToJpg(BufferedImage image) {
        BufferedImage jpgImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return Mono.just(jpgImage);
    }

    private Mono<Boolean> writeImage(BufferedImage image, String extension, ByteArrayOutputStream os) {
        return Mono.fromCallable(() -> ImageIO.write(image, extension, os));
    }

    private Mono<BufferedImage> readImage(InputStream inputStream) {
        return Mono.fromCallable(() -> ImageIO.read(inputStream));
    }

}
