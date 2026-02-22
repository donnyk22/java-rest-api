package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.ClassesDto;
import com.github.donnyk22.models.entities.Classes;
import com.github.donnyk22.models.forms.classes.ClassesCreateForm;

public class ClassesMapper {
    public static ClassesDto toBaseDto(Classes classes) {
        ClassesDto baseDto = new ClassesDto()
            .setId(classes.getId())
            .setClassName(classes.getClassName())
            .setGradeLevel(classes.getGradeLevel())
            .setAcademicYear(classes.getAcademicYear())
            .setCreatedAt(classes.getCreatedAt())
            .setUpdatedAt(classes.getUpdatedAt());
        return baseDto;
    }

    public static ClassesDto toBaseDtoWithHomeroomTeachers(Classes classes) {
        ClassesDto toBaseDtoWithHomeroomTeacher = toBaseDto(classes)
            .setHomeroomTeachers(classes.getHomeroomTeachers()
                .stream()
                .map(HomeroomTeachersMapper::toBaseDtoWithTeacher)
                .toList());
        return toBaseDtoWithHomeroomTeacher;
    }

    public static ClassesDto toDto(Classes classes) {
        ClassesDto dto = toBaseDtoWithHomeroomTeachers(classes)
            .setStudents(classes.getStudents()
                .stream()
                .map(StudentsMapper::toBaseDto)
                .toList());
        return dto;
    }

    public static Classes toEntity(Classes classes, ClassesCreateForm form) {
        classes.setClassName(form.getClassName())
            .setGradeLevel(form.getGradeLevel())
            .setAcademicYear(form.getAcademicYear());
        return classes;
    }
}
