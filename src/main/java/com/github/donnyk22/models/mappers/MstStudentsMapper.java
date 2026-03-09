package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.entities.MstStudents;
import com.github.donnyk22.models.forms.students.StudentsCreateForm;
import com.github.donnyk22.models.forms.students.StudentsUpdateForm;

public class MstStudentsMapper {
    public static MstStudentsDto toBaseDto(MstStudents students) {
        MstStudentsDto baseDto = new MstStudentsDto()
            .setId(students.getId())
            .setUserId(students.getUserId())
            .setClassId(students.getClassId())
            .setFullName(students.getFullName())
            .setGender(students.getGender())
            .setAddress(students.getAddress())
            .setPhone(students.getPhone())
            .setPhoto(students.getPhoto())
            .setVersion(students.getVersion())
            .setCreatedAt(students.getCreatedAt())
            .setUpdatedAt(students.getUpdatedAt());
        return baseDto;
    }

    public static MstStudentsDto toDto(MstStudents students) {
        MstStudentsDto dto = toBaseDto(students)
            .setClassroom(MstClassesMapper.toBaseDto(students.getClassroom()));
        return dto;
    }

    public static MstStudents toEntity(StudentsCreateForm form, String photo) {
        MstStudents students = new MstStudents()
            .setUserId(form.getUserId())
            .setClassId(form.getClassId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setAddress(form.getAddress())
            .setPhoto(photo);
        return students;
    }

    public static MstStudents toEntity(MstStudents students, StudentsUpdateForm form, String photo) {
        students.setUserId(form.getUserId())
            .setClassId(form.getClassId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setAddress(form.getAddress())
            .setPhoto(photo)
            .setVersion(form.getVersion());
        return students;
    }
}
