package com.github.donnyk22.utils;

import java.io.IOException;
import java.util.Base64;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.InternalServerErrorException;

public class MediaUtil {

    private static final Logger logger = LoggerFactory.getLogger(MediaUtil.class);

    public static String ToBase64(MultipartFile photo) throws Exception{
        try {
            if (photo == null) return null;
            validateImage(photo);
            byte[] fileBytes = photo.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            logger.error("Failed to convert file: " + e.getMessage());
            throw new Exception("Failed to convert file: " + e.getMessage());
        }
    }

    public static void validateImage(MultipartFile photo) {
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(photo.getInputStream());
            
            if (!detectedType.startsWith("image/")) {
                throw new BadRequestException("Invalid file type: " + detectedType);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("MIME type detection failed");
        }
    }

}
