package com.github.donnyk22.models.forms.attendances;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.donnyk22.models.forms.SearchForm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AttendancesFindForm extends SearchForm {
    private String academicYear;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // For Query Param (GET)
    private OffsetDateTime startRangeDate;
    private OffsetDateTime endRangeDate;
}
