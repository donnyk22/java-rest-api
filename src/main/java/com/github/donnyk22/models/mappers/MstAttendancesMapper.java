package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.MstAttendancesDto;
import com.github.donnyk22.models.entities.MstAttendances;
import com.github.donnyk22.models.forms.attendances.AttendancesCreateForm;

public class MstAttendancesMapper {

    private MstAttendancesMapper() {
        /* This utility class should not be instantiated */
    }

    public static MstAttendancesDto toBaseDto(MstAttendances attendances) {
        return new MstAttendancesDto()
                .setId(attendances.getId())
                .setStudentId(attendances.getStudentId())
                .setDate(attendances.getDate())
                .setStatus(attendances.getStatus())
                .setNote(attendances.getNote())
                .setCreatedAt(attendances.getCreatedAt());
    }

    public static MstAttendancesDto toDto(MstAttendances attendances) {
        return toBaseDto(attendances)
                .setStudent(MstStudentsMapper.toBaseDto(attendances.getStudentData()));
    }

    public static MstAttendances toEntity(MstAttendances attendances, AttendancesCreateForm form) {
        return attendances.setStudentId(form.getStudentId())
                .setDate(form.getDate())
                .setStatus(form.getStatus().name())
                .setNote(form.getNote());
    }
}
