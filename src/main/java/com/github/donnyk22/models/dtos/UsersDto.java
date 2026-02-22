package com.github.donnyk22.models.dtos;

import java.time.Instant;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UsersDto {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String role;
    private StudentsDto student;    
    private TeachersDto teacher;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String token;
    private Instant issuedAt;
    private Instant expiresAt;
}
