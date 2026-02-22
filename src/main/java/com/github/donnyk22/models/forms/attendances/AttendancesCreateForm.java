package com.github.donnyk22.models.forms.attendances;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.donnyk22.models.enums.StudentAttendanceStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AttendancesCreateForm {
    @NotNull(message = "Student ID is required")
    private Integer studentId;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date; // For Json body (POST)
    @NotNull(message = "Status is required")
    @Schema(implementation = StudentAttendanceStatus.class, allowableValues = {"PRESENT", "ABSENT", "LATE", "SICK", "PERMIT"})
    private StudentAttendanceStatus status;
    private String note;
}
