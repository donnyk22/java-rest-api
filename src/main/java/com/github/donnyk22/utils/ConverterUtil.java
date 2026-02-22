package com.github.donnyk22.utils;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> byte[] objectToBytes(T object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize object", e);
        }
    }

    public static <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize bytes", e);
        }
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
