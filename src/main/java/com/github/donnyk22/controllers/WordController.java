package com.github.donnyk22.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.donnyk22.models.forms.ApplicationLetterForm;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.word.WordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Word service APIs", description = "API for generating docx files")
@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @Operation(summary = "Generate Word example", description = "Generate Word application letter for an example")
    @GetMapping(value = "/generate-example", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<byte[]> generateWordApplicationLetter(@ModelAttribute @Valid ApplicationLetterForm form) {
        byte[] data = wordService.generateWordApplicationLetter(form);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=application_letter.docx")
                .contentType(MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(data);
    }

    @Operation(summary = "Generate Word example with existing template", description = "Generate Word application letter with existing template")
    @GetMapping(value = "/generate-example-with-existing-template", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<byte[]> generateWordApplicationLetterWithExistingTemplate(
            @ModelAttribute @Valid ApplicationLetterForm form) {
        byte[] data = wordService.generateWordApplicationLetterWithExistingTemplate(form);
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=application_letter_with_existing_template.docx")
                .contentType(MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(data);
    }

    @Operation(summary = "Export student data", description = "Export student data as Word file")
    @GetMapping(value = "/export-student-data", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<byte[]> generateStudentsData(@ModelAttribute @Valid StudentsFindForm form) {
        byte[] data = wordService.generateWordStudentData(form);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=students-export.docx")
                .contentType(MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(data);
    }
}
