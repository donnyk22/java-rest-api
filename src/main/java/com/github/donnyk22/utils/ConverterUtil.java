package com.github.donnyk22.utils;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConverterUtil {

    private final ObjectMapper objectMapper;

    public <T> byte[] objectToBytes(T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize object", e);
        }
    }

    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize bytes", e);
        }
    }

    public String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public <T> String genericToJson(T properties) {
        try {
            if (properties == null)
                return "{}";
            return objectMapper.writeValueAsString(properties);
        } catch (Exception e) {
            log.warn("Failed to convert object to JSON: " + e.getMessage());
            return "{}";
        }
    }
}
