package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class HomeroomTeachersDto {
    private Integer id;
    private Integer classId;
    private ClassesDto classroom;
    private Integer teacherId;
    private TeachersDto teacher;
    private OffsetDateTime createdAt;
}
