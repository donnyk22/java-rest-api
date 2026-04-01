package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstTeachersDto;
import com.github.donnyk22.models.entities.MstTeachers;
import com.github.donnyk22.models.forms.teachers.TeachersCreateForm;
import com.github.donnyk22.models.forms.teachers.TeachersUpdateForm;

public class MstTeachersMapper {

    private MstTeachersMapper() {
        /* This utility class should not be instantiated */
    }

    public static MstTeachersDto toBaseDto(MstTeachers teachers) {
        return new MstTeachersDto()
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
    }

    public static MstTeachersDto toDto(MstTeachers teachers) {
        return toBaseDto(teachers)
                .setHomeroomTeachers(teachers.getHomeroomTeachers()
                        .stream()
                        .map(MstHomeroomTeachersMapper::toBaseDtoWithClassroom)
                        .toList());
    }

    public static MstTeachers toEntity(TeachersCreateForm form, String photo) {
        return new MstTeachers()
                .setUserId(form.getUserId())
                .setFullName(form.getFullName())
                .setGender(form.getGender().name().charAt(0))
                .setPhone(form.getPhone())
                .setAddress(form.getAddress())
                .setPhoto(photo);
    }

    public static MstTeachers toEntity(MstTeachers teachers, TeachersUpdateForm form, String photo) {
        return teachers.setUserId(form.getUserId())
                .setFullName(form.getFullName())
                .setGender(form.getGender().name().charAt(0))
                .setPhone(form.getPhone())
                .setAddress(form.getAddress())
                .setPhoto(photo)
                .setVersion(form.getVersion());
    }
}