package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.StudentsDto;
import com.github.donnyk22.models.entities.Students;

public class StudentsMapperDto {
    public static StudentsDto toBaseDto(Students students) {
        StudentsDto baseDto = new StudentsDto()
            .setId(students.getId())
            .setUserId(students.getUserId())
            .setClassId(students.getClassId())
            .setFullName(students.getFullName())
            .setGender(students.getGender())
            .setAddress(students.getAddress())
            .setPhoto(students.getPhoto())
            .setCreatedAt(students.getCreatedAt())
            .setUpdatedAt(students.getUpdatedAt());
        return baseDto;
    }

    public static StudentsDto toDto(Students students) {
        StudentsDto dto = toBaseDto(students)
            .setClassroom(ClassesMapper.toBaseDtoWithHomeroomTeachers(students.getClassroom()));
        return dto;
    }
}
