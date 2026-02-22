package com.github.donnyk22.models.dtos;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AttendancesDto {
    private Integer id;
    private Integer studentId;
    private StudentsDto student;
    private LocalDate date;
    private String status;
    private String note;
    private OffsetDateTime createdAt;
}
