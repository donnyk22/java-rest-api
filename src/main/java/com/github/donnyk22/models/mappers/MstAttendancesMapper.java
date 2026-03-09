package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstAttendancesDto;
import com.github.donnyk22.models.entities.MstAttendances;
import com.github.donnyk22.models.forms.attendances.AttendancesCreateForm;

public class MstAttendancesMapper {
    public static MstAttendancesDto toBaseDto(MstAttendances attendances) {
        MstAttendancesDto baseDto = new MstAttendancesDto()
            .setId(attendances.getId())
            .setStudentId(attendances.getStudentId())
            .setDate(attendances.getDate())
            .setStatus(attendances.getStatus())
            .setNote(attendances.getNote())
            .setCreatedAt(attendances.getCreatedAt());
        return baseDto;
    }

    public static MstAttendancesDto toDto(MstAttendances attendances) {
        MstAttendancesDto dto = toBaseDto(attendances)
            .setStudent(MstStudentsMapper.toBaseDto(attendances.getStudentData()));
        return dto;
    }

    public static MstAttendances toEntity(MstAttendances attendances, AttendancesCreateForm form) {
        attendances.setStudentId(form.getStudentId())
            .setDate(form.getDate())
            .setStatus(form.getStatus().name())
            .setNote(form.getNote());
        return attendances;
    }
}
