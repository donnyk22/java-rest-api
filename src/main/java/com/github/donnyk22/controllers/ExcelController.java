package com.github.donnyk22.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.excel.ExcelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Excel service APIs", description = "Excel tools related")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/excel")
public class ExcelController {

    private final ExcelService excelService;

    @Operation(summary = "Export data to Excel", description = "Export find student data to Excel format")
    @GetMapping(value = "/export-student-data", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportToNewExcel(@ModelAttribute @Valid StudentsFindForm form) {
        byte[] data = excelService.exportToNewExcel(form);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=students-export.xlsx")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @Operation(summary = "Export data to Excel", description = "Export find student data to existing Excel template")
    @GetMapping(value = "/export-with-existing-template", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportToExistingExcelTemplate(@ModelAttribute @Valid StudentsFindForm form) {
        byte[] data = excelService.exportToExistingExcelTemplate(form);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"students-export-existing-template.xlsx\"")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @Operation(summary = "Import Excel file", description = "Import and read them as a json array")
    @PostMapping(value = "/import-excel-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> importExcelFile(
            @ModelAttribute @Parameter(required = true) MultipartFile file) {
        List<Map<String, Object>> result = excelService.readImportedExcelFile(file);
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Import Excel file as json successfully",
                result);
        return ResponseEntity.ok(response);
    }

}
