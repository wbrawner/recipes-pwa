package com.wbrawner.recipes.controller;

import com.wbrawner.recipes.model.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Secured("USER")
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final File imagesDir;
    private static final List<String> imageExtensions = Arrays.asList(
            "png",
            "jpg",
            "jpeg"
    );

    @Autowired
    public ImageController(@Value("${recipes.storage.images}") String imagesDir) {
        this.imagesDir = new File(imagesDir);
        if (!this.imagesDir.exists() && !this.imagesDir.mkdirs()) {
            throw new RuntimeException("Unable to create images dir: " + imagesDir);
        }
        if (!this.imagesDir.canRead()) {
            throw new RuntimeException("Unable to read images dir: " + imagesDir);
        }
        if (!this.imagesDir.canWrite()) {
            throw new RuntimeException("Unable to write to images dir: " + imagesDir);
        }
    }

    @PostMapping
    public Mono<ServerResponse> saveImage(@RequestParam("image") FilePart image) {
        if (image == null) {
            return ServerResponse.badRequest().bodyValue("No image provided");
        }
        var imageParts = image.filename().split("\\.");
        if (!imageExtensions.contains(imageParts[imageParts.length - 1])) {
            return ServerResponse.badRequest().bodyValue("File must be an image");
        }
        var imageId = UUID.randomUUID().toString();
        File imageFile = new File(imagesDir, imageId);
        return image.transferTo(imageFile)
                .then(ServerResponse.ok().bodyValue(new ImageResponse(imageId)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteImage(@PathVariable("id") String imageId) {
        return Mono.fromRunnable(() -> {
            try {
                var imageFile = new File(imagesDir, imageId);
                //noinspection BlockingMethodInNonBlockingContext
                Files.deleteIfExists(imageFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
