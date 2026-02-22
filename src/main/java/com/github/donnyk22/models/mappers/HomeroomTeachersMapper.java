package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.HomeroomTeachersDto;
import com.github.donnyk22.models.entities.HomeroomTeachers;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersCreateForm;

public class HomeroomTeachersMapper {
    public static HomeroomTeachersDto toBaseDto(HomeroomTeachers homeroomTeachers) {
        HomeroomTeachersDto baseDto = new HomeroomTeachersDto()
            .setId(homeroomTeachers.getId())
            .setClassId(homeroomTeachers.getClassId())
            .setTeacherId(homeroomTeachers.getTeacherId())
            .setCreatedAt(homeroomTeachers.getCreatedAt());
        return baseDto;
    }

    public static HomeroomTeachersDto toBaseDtoWithClassroom(HomeroomTeachers homeroomTeachers) {
        HomeroomTeachersDto toBaseDtoWithClassroom = toBaseDto(homeroomTeachers)
            .setClassroom(ClassesMapper.toBaseDto(homeroomTeachers.getClassroom()));
        return toBaseDtoWithClassroom;
    }

    public static HomeroomTeachersDto toBaseDtoWithTeacher(HomeroomTeachers homeroomTeachers) {
        HomeroomTeachersDto toBaseDtoWithTeacher = toBaseDto(homeroomTeachers)
            .setTeacher(TeachersMapper.toBaseDto(homeroomTeachers.getTeacher()));
        return toBaseDtoWithTeacher;
    }

    public static HomeroomTeachersDto toDto(HomeroomTeachers homeroomTeachers) {
        HomeroomTeachersDto dto = toBaseDtoWithClassroom(homeroomTeachers)
            .setTeacher(TeachersMapper.toBaseDto(homeroomTeachers.getTeacher()));
        return dto;
    }

    public static HomeroomTeachers toEntity(HomeroomTeachersCreateForm form) {
        HomeroomTeachers homeroomTeachers = new HomeroomTeachers()
            .setClassId(form.getClassId())
            .setTeacherId(form.getTeacherId());
        return homeroomTeachers;
    }
}
