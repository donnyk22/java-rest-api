package com.github.donnyk22.models.dtos;

import java.time.Instant;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MstUsersDto {
    private Integer id;
    private String username;
    private String email;
    private String photo;
    private String name;
    private String role;
    private Boolean mfaEnabled;
    private MstStudentsDto student;
    private MstTeachersDto teacher;
    private Integer version;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String token;
    private Instant issuedAt;
    private Instant expiresAt;
}
