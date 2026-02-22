package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TeachersDto {
    private Integer id;
    private Integer userId;
    private String fullName;
    private Character gender;
    private String phone;
    private String address;
    private String photo;
    private List<HomeroomTeachersDto> homeroomTeachers;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
