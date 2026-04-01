package com.github.donnyk22.utils;

import java.io.IOException;
import java.util.Base64;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.InternalServerErrorException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MediaUtil {

    public String toBase64(MultipartFile photo) throws Exception {
        try {
            if (photo == null)
                return null;
            validateImage(photo);
            byte[] fileBytes = photo.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            log.error("Failed to convert file: " + e.getMessage());
            throw new InternalServerErrorException("Failed to convert file: " + e.getMessage());
        }
    }

    public void validateImage(MultipartFile photo) {
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(photo.getInputStream());

            if (!detectedType.startsWith("image/")) {
                throw new BadRequestException("Invalid file type: " + detectedType);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("MIME type detection failed", e);
        }
    }

}
