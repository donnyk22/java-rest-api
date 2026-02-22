package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ClassesDto {
    private Integer id;
    private String className;
    private String gradeLevel;
    private String academicYear;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<HomeroomTeachersDto> homeroomTeachers;
    private List<StudentsDto> students;
}
