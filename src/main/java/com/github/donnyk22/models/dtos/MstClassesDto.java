package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MstClassesDto {
    private Integer id;
    private String className;
    private Integer gradeLevel;
    private String academicYear;
    private Integer version;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<MstHomeroomTeachersDto> homeroomTeachers;
    private List<MstStudentsDto> students;
}
