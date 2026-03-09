package com.github.donnyk22.utils;

import java.lang.reflect.Method;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ConflictException;

import jakarta.persistence.Table;

public class EntityUtil {

    public static String getTableName(Object entity) {
        if (entity == null) return null;
        Table table = entity.getClass().getAnnotation(Table.class);
        if (table != null && !table.name().isEmpty()) {
            return table.name();
        }
        return entity.getClass().getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static <T> Integer getIdByReflection(T data) {
        if (data == null) return null;
        try {
            Method method = data.getClass().getMethod("getId");
            Object result = method.invoke(data);
            return (Integer) result;
        } catch (Exception e) {
            return null; 
        }
    }

    public static void compareVersion(Integer currentVersion, Integer providedVersion) {
        if (currentVersion < providedVersion) {
            throw new BadRequestException("Invalid version. Please refresh and try again.");
        }
        if (!currentVersion.equals(providedVersion)) {
            throw new ConflictException("Data is already updated by another user. Please refresh and try again.");
        }
    }
}
