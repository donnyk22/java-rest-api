package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.entities.MstStudents;
import com.github.donnyk22.models.forms.students.StudentsCreateForm;
import com.github.donnyk22.models.forms.students.StudentsUpdateForm;

public class MstStudentsMapper {

    private MstStudentsMapper() {
        /* This utility class should not be instantiated */
    }

    public static MstStudentsDto toBaseDto(MstStudents students) {
        return new MstStudentsDto()
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
    }

    public static MstStudentsDto toDto(MstStudents students) {
        return toBaseDto(students)
                .setClassroom(MstClassesMapper.toBaseDto(students.getClassroom()));
    }

    public static MstStudents toEntity(StudentsCreateForm form, String photo) {
        return new MstStudents()
                .setUserId(form.getUserId())
                .setClassId(form.getClassId())
                .setFullName(form.getFullName())
                .setGender(form.getGender().name().charAt(0))
                .setAddress(form.getAddress())
                .setPhoto(photo);
    }

    public static MstStudents toEntity(MstStudents students, StudentsUpdateForm form, String photo) {
        return students.setUserId(form.getUserId())
                .setClassId(form.getClassId())
                .setFullName(form.getFullName())
                .setGender(form.getGender().name().charAt(0))
                .setAddress(form.getAddress())
                .setPhoto(photo)
                .setVersion(form.getVersion());
    }
}
