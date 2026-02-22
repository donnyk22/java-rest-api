package com.github.donnyk22.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static String PATH;
    private static String MAX_SIZE;

    //injection for static variables
    @Value("${upload.profile-pic.max-size}")
    public void setMaxSize(String maxSize) {
        FileUtil.MAX_SIZE = maxSize;
    }

    @Value("${upload.path.profile-pic}")
    public void setPath(String path) {
        FileUtil.PATH = path;
    }

    public static String saveProfilePic(MultipartFile photo) {
        try{
            if(photo == null) return null;

            long maxSizeBytes = DataSize.parse(MAX_SIZE).toBytes();
            if (photo.getSize() > maxSizeBytes) {
                throw new RuntimeException("Max file size is " + MAX_SIZE);
            }

            Path uploadPath = Paths.get(PATH);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(photo.getOriginalFilename());
            Path path = uploadPath.resolve(fileName);

            Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return path.toString();
        } catch (IOException e) {
            logger.error("Disk/File error: " + e.getMessage());
            throw new RuntimeException("Failed to save profile picture: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error to save profile picture: " + e.getMessage());
            throw new RuntimeException("Unexpected error to save profile picture: " + e.getMessage());
        }
    }

    public static void deleteProfilePic(String filePath) {
        if (!StringUtils.hasLength(filePath)) {
            return;
        }

        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
            logger.info("Profile picture deleted successfully: " + filePath);
        } catch (IOException e) {
            logger.error("Disk/File error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error deleting profile picture: " + e.getMessage());
        }
    }
}
