package com.github.donnyk22.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.InternalServerErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    private final MediaUtil mediaUtil;

    @Value("${upload.profile-pic.path}")
    private String path;

    @Value("${upload.profile-pic.max-size}")
    private String maxSize;

    public String saveProfilePic(MultipartFile photo) {
        try {
            if (photo == null)
                return null;

            mediaUtil.validateImage(photo);

            String originalFilename = photo.getOriginalFilename();
            if (originalFilename == null) {
                throw new BadRequestException("File name is missing");
            }

            long maxSizeBytes = DataSize.parse(maxSize).toBytes();
            if (photo.getSize() > maxSizeBytes) {
                throw new BadRequestException("Max file size is " + maxSize);
            }

            Path uploadPath = Paths.get(path);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(originalFilename);
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (IOException e) {
            log.error("Disk/File error: " + e.getMessage());
            throw new InternalServerErrorException("Failed to save profile picture: " + e.getMessage());
        }
    }

    public void deleteProfilePic(String filePath) {
        if (!StringUtils.hasText(filePath))
            return;

        try {
            Path targetPath = Paths.get(filePath);
            Files.deleteIfExists(targetPath);
            log.info("Profile picture deleted successfully: " + filePath);
        } catch (IOException e) {
            log.error("Disk/File error: " + e.getMessage());
        }
    }
}
