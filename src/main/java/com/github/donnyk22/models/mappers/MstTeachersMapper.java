package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstTeachersDto;
import com.github.donnyk22.models.entities.MstTeachers;
import com.github.donnyk22.models.forms.teachers.TeachersCreateForm;
import com.github.donnyk22.models.forms.teachers.TeachersUpdateForm;

public class MstTeachersMapper {
    public static MstTeachersDto toBaseDto(MstTeachers teachers) {
        MstTeachersDto baseDto = new MstTeachersDto()
            .setId(teachers.getId())
            .setUserId(teachers.getUserId())
            .setFullName(teachers.getFullName())
            .setGender(teachers.getGender())
            .setPhone(teachers.getPhone())
            .setAddress(teachers.getAddress())
            .setPhoto(teachers.getPhoto())
            .setVersion(teachers.getVersion())
            .setCreatedAt(teachers.getCreatedAt())
            .setUpdatedAt(teachers.getUpdatedAt());
        return baseDto;
    }

    public static MstTeachersDto toDto(MstTeachers teachers) {
        MstTeachersDto dto = toBaseDto(teachers)
            .setHomeroomTeachers(teachers.getHomeroomTeachers()
                .stream()
                .map(MstHomeroomTeachersMapper::toBaseDtoWithClassroom)
                .toList());
        return dto;
    }

    public static MstTeachers toEntity(TeachersCreateForm form, String photo) {
        MstTeachers teachers = new MstTeachers()
            .setUserId(form.getUserId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setPhone(form.getPhone())
            .setAddress(form.getAddress())
            .setPhoto(photo);
        return teachers;
    }

    public static MstTeachers toEntity(MstTeachers teachers, TeachersUpdateForm form, String photo) {
        teachers.setUserId(form.getUserId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setPhone(form.getPhone())
            .setAddress(form.getAddress())
            .setPhoto(photo)
            .setVersion(form.getVersion());
        return teachers;
    }
}