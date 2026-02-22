package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class StudentsDto {
    private Integer id;
    private Integer userId;
    private Integer classId;
    private ClassesDto classroom;
    private String fullName;
    private Character gender;
    private String address;
    private String photo;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
