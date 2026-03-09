package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MstHomeroomTeachersDto {
    private Integer id;
    private Integer classId;
    private MstClassesDto classroom;
    private Integer teacherId;
    private MstTeachersDto teacher;
    private OffsetDateTime createdAt;
}
