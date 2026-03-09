package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstHomeroomTeachersDto;
import com.github.donnyk22.models.entities.MstHomeroomTeachers;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersCreateForm;

public class MstHomeroomTeachersMapper {
    public static MstHomeroomTeachersDto toBaseDto(MstHomeroomTeachers homeroomTeachers) {
        MstHomeroomTeachersDto baseDto = new MstHomeroomTeachersDto()
            .setId(homeroomTeachers.getId())
            .setClassId(homeroomTeachers.getClassId())
            .setTeacherId(homeroomTeachers.getTeacherId())
            .setCreatedAt(homeroomTeachers.getCreatedAt());
        return baseDto;
    }

    public static MstHomeroomTeachersDto toBaseDtoWithClassroom(MstHomeroomTeachers homeroomTeachers) {
        MstHomeroomTeachersDto toBaseDtoWithClassroom = toBaseDto(homeroomTeachers)
            .setClassroom(MstClassesMapper.toBaseDto(homeroomTeachers.getClassData()));
        return toBaseDtoWithClassroom;
    }

    public static MstHomeroomTeachersDto toBaseDtoWithTeacher(MstHomeroomTeachers homeroomTeachers) {
        MstHomeroomTeachersDto toBaseDtoWithTeacher = toBaseDto(homeroomTeachers)
            .setTeacher(MstTeachersMapper.toBaseDto(homeroomTeachers.getTeacherData()));
        return toBaseDtoWithTeacher;
    }

    public static MstHomeroomTeachersDto toDto(MstHomeroomTeachers homeroomTeachers) {
        MstHomeroomTeachersDto dto = toBaseDtoWithClassroom(homeroomTeachers)
            .setTeacher(MstTeachersMapper.toBaseDto(homeroomTeachers.getTeacherData()));
        return dto;
    }

    public static MstHomeroomTeachers toEntity(HomeroomTeachersCreateForm form) {
        MstHomeroomTeachers homeroomTeachers = new MstHomeroomTeachers()
            .setClassId(form.getClassId())
            .setTeacherId(form.getTeacherId());
        return homeroomTeachers;
    }
}
