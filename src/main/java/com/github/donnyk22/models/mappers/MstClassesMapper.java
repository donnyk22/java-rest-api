package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstClassesDto;
import com.github.donnyk22.models.entities.MstClasses;
import com.github.donnyk22.models.forms.classes.ClassesCreateForm;
import com.github.donnyk22.models.forms.classes.ClassesUpdateForm;

public class MstClassesMapper {

    private MstClassesMapper() {
        /* This utility class should not be instantiated */
    }

    public static MstClassesDto toBaseDto(MstClasses classes) {
        return new MstClassesDto()
                .setId(classes.getId())
                .setClassName(classes.getClassName())
                .setGradeLevel(classes.getGradeLevel())
                .setAcademicYear(classes.getAcademicYear())
                .setVersion(classes.getVersion())
                .setCreatedAt(classes.getCreatedAt())
                .setUpdatedAt(classes.getUpdatedAt());
    }

    public static MstClassesDto toBaseDtoWithHomeroomTeachers(MstClasses classes) {
        return toBaseDto(classes)
                .setHomeroomTeachers(classes.getHomeroomTeachers()
                        .stream()
                        .map(MstHomeroomTeachersMapper::toBaseDtoWithTeacher)
                        .toList());
    }

    public static MstClassesDto toDto(MstClasses classes) {
        return toBaseDtoWithHomeroomTeachers(classes)
                .setStudents(classes.getStudents()
                        .stream()
                        .map(MstStudentsMapper::toBaseDto)
                        .toList());
    }

    public static MstClasses toEntity(ClassesCreateForm form) {
        return new MstClasses()
                .setClassName(form.getClassName())
                .setGradeLevel(form.getGradeLevel())
                .setAcademicYear(form.getAcademicYear());
    }

    public static MstClasses toEntity(MstClasses classes, ClassesUpdateForm form) {
        return classes.setClassName(form.getClassName())
                .setGradeLevel(form.getGradeLevel())
                .setAcademicYear(form.getAcademicYear());
    }
}
