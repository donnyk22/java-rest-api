package com.github.donnyk22.models.dtos;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private Instant timestamp;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }
}