package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.dtos.MstTeachersDto;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.forms.users.UserRegisterForm;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;

public class MstUsersMapper {
    public static MstUsersDto toBaseDto(MstUsers users) {
        MstUsersDto toBaseDto = new MstUsersDto()
            .setId(users.getId())
            .setUsername(users.getUsername())
            .setEmail(users.getEmail())
            .setPhoto(users.getPhoto())
            .setRole(users.getRole())
            .setVersion(users.getVersion())
            .setCreatedAt(users.getCreatedAt())
            .setUpdatedAt(users.getUpdatedAt());
        return toBaseDto;
    }

    public static MstUsersDto toDto(MstUsers users) {
        String fullname = null;
        MstStudentsDto studentDto = null;
        MstTeachersDto teacherDto = null;
        if (users.getStudentData() != null) {
            fullname = users.getStudentData().getFullName();
            studentDto = MstStudentsMapper.toBaseDto(users.getStudentData());
        } else if (users.getTeacherData() != null) {
            fullname = users.getTeacherData().getFullName();
            teacherDto = MstTeachersMapper.toBaseDto(users.getTeacherData());
        }
        MstUsersDto dto = toBaseDto(users)
            .setName(fullname)
            .setStudent(studentDto)
            .setTeacher(teacherDto);
        return dto;
    }

    public static MstUsers toEntity(UserRegisterForm form, String encryptedPassword) {
        MstUsers users = new MstUsers()
            .setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setRole(form.getRole().name())
            .setPassword(encryptedPassword);
        return users;
    }

    public static MstUsers toEntity(UsersCreateForm form, String photo, String encryptedPassword) {
        MstUsers users = new MstUsers()
            .setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setPhoto(photo)
            .setRole(form.getRole().name())
            .setIsActive(form.getIsActive())
            .setPassword(encryptedPassword);
        return users;
    }

    public static MstUsers toEntity(MstUsers user, UsersUpdateForm form, String photo) {
        user.setUsername(form.getUsername())
            .setEmail(form.getEmail())
            .setPhoto(photo)
            .setRole(form.getRole().name())
            .setIsActive(form.getIsActive());
        return user;
    }
}
