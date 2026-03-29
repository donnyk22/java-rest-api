package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstClassesDto;
import com.github.donnyk22.models.entities.MstClasses;
import com.github.donnyk22.models.forms.classes.ClassesCreateForm;
import com.github.donnyk22.models.forms.classes.ClassesUpdateForm;

public class MstClassesMapper {
    public static MstClassesDto toBaseDto(MstClasses classes) {
        MstClassesDto baseDto = new MstClassesDto()
                .setId(classes.getId())
                .setClassName(classes.getClassName())
                .setGradeLevel(classes.getGradeLevel())
                .setAcademicYear(classes.getAcademicYear())
                .setVersion(classes.getVersion())
                .setCreatedAt(classes.getCreatedAt())
                .setUpdatedAt(classes.getUpdatedAt());
        return baseDto;
    }

    public static MstClassesDto toBaseDtoWithHomeroomTeachers(MstClasses classes) {
        MstClassesDto toBaseDtoWithHomeroomTeacher = toBaseDto(classes)
                .setHomeroomTeachers(classes.getHomeroomTeachers()
                        .stream()
                        .map(MstHomeroomTeachersMapper::toBaseDtoWithTeacher)
                        .toList());
        return toBaseDtoWithHomeroomTeacher;
    }

    public static MstClassesDto toDto(MstClasses classes) {
        MstClassesDto dto = toBaseDtoWithHomeroomTeachers(classes)
                .setStudents(classes.getStudents()
                        .stream()
                        .map(MstStudentsMapper::toBaseDto)
                        .toList());
        return dto;
    }

    public static MstClasses toEntity(ClassesCreateForm form) {
        MstClasses classes = new MstClasses()
                .setClassName(form.getClassName())
                .setGradeLevel(form.getGradeLevel())
                .setAcademicYear(form.getAcademicYear());
        return classes;
    }

    public static MstClasses toEntity(MstClasses classes, ClassesUpdateForm form) {
        classes.setClassName(form.getClassName())
                .setGradeLevel(form.getGradeLevel())
                .setAcademicYear(form.getAcademicYear());
        return classes;
    }
}
