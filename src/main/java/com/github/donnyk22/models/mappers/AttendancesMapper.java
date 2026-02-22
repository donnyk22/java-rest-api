package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.AttendancesDto;
import com.github.donnyk22.models.entities.Attendances;
import com.github.donnyk22.models.forms.attendances.AttendancesCreateForm;

public class AttendancesMapper {
    public static AttendancesDto toBaseDto(Attendances attendances) {
        AttendancesDto baseDto = new AttendancesDto()
            .setId(attendances.getId())
            .setStudentId(attendances.getStudentId())
            .setDate(attendances.getDate())
            .setStatus(attendances.getStatus())
            .setNote(attendances.getNote())
            .setCreatedAt(attendances.getCreatedAt());
        return baseDto;
    }

    public static AttendancesDto toDto(Attendances attendances) {
        AttendancesDto dto = toBaseDto(attendances)
            .setStudent(StudentsMapper.toBaseDto(attendances.getStudent()));
        return dto;
    }

    public static Attendances toEntity(Attendances attendances, AttendancesCreateForm form) {
        attendances.setStudentId(form.getStudentId())
            .setDate(form.getDate())
            .setStatus(form.getStatus().name())
            .setNote(form.getNote());
        return attendances;
    }
}
