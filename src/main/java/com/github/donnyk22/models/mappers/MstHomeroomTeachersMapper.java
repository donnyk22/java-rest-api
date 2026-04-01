package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstHomeroomTeachersDto;
import com.github.donnyk22.models.entities.MstHomeroomTeachers;
import com.github.donnyk22.models.forms.homeroomteachers.HomeroomTeachersCreateForm;

public class MstHomeroomTeachersMapper {

    private MstHomeroomTeachersMapper() {
        /* This utility class should not be instantiated */
    }

    public static MstHomeroomTeachersDto toBaseDto(MstHomeroomTeachers homeroomTeachers) {
        return new MstHomeroomTeachersDto()
                .setId(homeroomTeachers.getId())
                .setClassId(homeroomTeachers.getClassId())
                .setTeacherId(homeroomTeachers.getTeacherId())
                .setCreatedAt(homeroomTeachers.getCreatedAt());
    }

    public static MstHomeroomTeachersDto toBaseDtoWithClassroom(MstHomeroomTeachers homeroomTeachers) {
        return toBaseDto(homeroomTeachers)
                .setClassroom(MstClassesMapper.toBaseDto(homeroomTeachers.getClassData()));
    }

    public static MstHomeroomTeachersDto toBaseDtoWithTeacher(MstHomeroomTeachers homeroomTeachers) {
        return toBaseDto(homeroomTeachers)
                .setTeacher(MstTeachersMapper.toBaseDto(homeroomTeachers.getTeacherData()));
    }

    public static MstHomeroomTeachersDto toDto(MstHomeroomTeachers homeroomTeachers) {
        return toBaseDtoWithClassroom(homeroomTeachers)
                .setTeacher(MstTeachersMapper.toBaseDto(homeroomTeachers.getTeacherData()));
    }

    public static MstHomeroomTeachers toEntity(HomeroomTeachersCreateForm form) {
        return new MstHomeroomTeachers()
                .setClassId(form.getClassId())
                .setTeacherId(form.getTeacherId());
    }
}
