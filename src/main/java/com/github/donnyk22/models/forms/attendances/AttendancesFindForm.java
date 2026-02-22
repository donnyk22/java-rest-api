package com.github.donnyk22.models.forms.attendances;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.donnyk22.models.forms.SearchForm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AttendancesFindForm extends SearchForm {
    @Schema(example = "2025/2026", description = "Academic Year (YYYY/YYYY)")
    private String academicYear;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // For Query Param (GET)
    @Schema(example = "2026-01-21", description = "Start date for filtering (YYYY-MM-DD)")
    private LocalDate startRangeDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // For Query Param (GET)
    @Schema(example = "2026-01-31", description = "End date for filtering (YYYY-MM-DD)")
    private LocalDate endRangeDate;
}
