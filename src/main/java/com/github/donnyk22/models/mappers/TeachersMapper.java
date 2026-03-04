package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.TeachersDto;
import com.github.donnyk22.models.entities.Teachers;
import com.github.donnyk22.models.forms.teachers.TeachersCreateForm;
import com.github.donnyk22.models.forms.teachers.TeachersUpdateForm;

public class TeachersMapper {
    public static TeachersDto toBaseDto(Teachers teachers) {
        TeachersDto baseDto = new TeachersDto()
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

    public static TeachersDto toDto(Teachers teachers) {
        TeachersDto dto = toBaseDto(teachers)
            .setHomeroomTeachers(teachers.getHomeroomTeachers()
                .stream()
                .map(HomeroomTeachersMapper::toBaseDtoWithClassroom)
                .toList());
        return dto;
    }

    public static Teachers toEntity(TeachersCreateForm form, String photo) {
        Teachers teachers = new Teachers()
            .setUserId(form.getUserId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setPhone(form.getPhone())
            .setAddress(form.getAddress())
            .setPhoto(photo);
        return teachers;
    }

    public static Teachers toEntity(Teachers teachers, TeachersUpdateForm form, String photo) {
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