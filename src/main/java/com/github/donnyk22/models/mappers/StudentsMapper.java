package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.StudentsDto;
import com.github.donnyk22.models.entities.Students;
import com.github.donnyk22.models.forms.students.StudentsCreateForm;
import com.github.donnyk22.models.forms.students.StudentsUpdateForm;

public class StudentsMapper {
    public static StudentsDto toBaseDto(Students students) {
        StudentsDto baseDto = new StudentsDto()
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

    public static StudentsDto toDto(Students students) {
        StudentsDto dto = toBaseDto(students)
            .setClassroom(ClassesMapper.toBaseDto(students.getClassroom()));
        return dto;
    }

    public static Students toEntity(StudentsCreateForm form, String photo) {
        Students students = new Students()
            .setUserId(form.getUserId())
            .setClassId(form.getClassId())
            .setFullName(form.getFullName())
            .setGender(form.getGender().name().charAt(0))
            .setAddress(form.getAddress())
            .setPhoto(photo);
        return students;
    }

    public static Students toEntity(Students students, StudentsUpdateForm form, String photo) {
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
