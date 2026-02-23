package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.StudentsDto;
import com.github.donnyk22.models.dtos.TeachersDto;
import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.entities.Users;
import com.github.donnyk22.models.forms.users.UserRegisterForm;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;

public class UsersMapper {
    public static UsersDto toBaseDto(Users users) {
        UsersDto toBaseDto = new UsersDto()
            .setId(users.getId())
            .setUsername(users.getUsername())
            .setEmail(users.getEmail())
            .setRole(users.getRole())
            .setCreatedAt(users.getCreatedAt())
            .setUpdatedAt(users.getUpdatedAt());
        return toBaseDto;
    }

    public static UsersDto toDto(Users users) {
        String fullname = null;
        StudentsDto studentDto = null;
        TeachersDto teacherDto = null;
        if (users.getStudentData() != null) {
            fullname = users.getStudentData().getFullName();
            studentDto = StudentsMapper.toBaseDto(users.getStudentData());
        } else if (users.getTeacherData() != null) {
            fullname = users.getTeacherData().getFullName();
            teacherDto = TeachersMapper.toBaseDto(users.getTeacherData());
        }
        UsersDto dto = toBaseDto(users)
            .setName(fullname)
            .setStudent(studentDto)
            .setTeacher(teacherDto);
        return dto;
    }

    public static Users toRegisterEntity(UserRegisterForm form, String encryptedPassword) {
        Users users = new Users()
            .setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setRole(form.getRole().name())
            .setPassword(encryptedPassword);
        return users;
    }

    public static Users toCreateUserEntity(UsersCreateForm form, String encryptedPassword) {
        Users users = new Users()
            .setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setPhoto(form.getPhoto())
            .setRole(form.getRole().name())
            .setIsActive(form.getIsActive())
            .setPassword(encryptedPassword);
        return users;
    }

    public static Users toUpdateUserEntity(Users user, UsersUpdateForm form) {
        user.setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setPhoto(form.getPhoto())
            .setRole(form.getRole().name())
            .setIsActive(form.getIsActive());
        return user;
    }
}
