package com.augusto.backend.service;

import com.augusto.backend.service.exception.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                .flatMap(inputStream -> readImage(inputStream.asInputStream())
                        .flatMap(image -> {
                            if (PNG_FORMAT.equals(fileExtension)) {
                                return pngToJpg(image);
                            } else {
                                return Mono.just(image);
                            }
                        }).doOnNext(image -> DataBufferUtils.release(inputStream)));
    }

    public Mono<InputStream> getInputStream(BufferedImage image, String extension) {
        return writeImage(image, extension)
                .map(os -> new ByteArrayInputStream(os.toByteArray()));
    }

    public BufferedImage cropImage(BufferedImage image) {
        Integer minSizeSide = Math.min(image.getHeight(), image.getWidth());

        return Scalr.crop(image,
                (image.getWidth() / 2) - (minSizeSide / 2),
                (image.getHeight() / 2) - (minSizeSide / 2),
                minSizeSide,
                minSizeSide);
    }

    public BufferedImage resize (BufferedImage image, Integer size) {
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, size);
    }

    private Mono<BufferedImage> pngToJpg(BufferedImage image) {
        BufferedImage jpgImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return Mono.just(jpgImage);
    }

    private Mono<ByteArrayOutputStream> writeImage(BufferedImage image, String extension) {
        return Mono.fromCallable(() -> {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, extension, os);
            return os;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<BufferedImage> readImage(InputStream inputStream) {
        return Mono.fromCallable(() -> ImageIO.read(inputStream))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
