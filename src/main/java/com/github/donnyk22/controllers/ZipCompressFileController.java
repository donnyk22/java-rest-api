package com.github.donnyk22.controllers;

import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.zip.ZipCompressFileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Zip Compress File", description = "Generate and download zip file with excel and word data")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/zip")
public class ZipCompressFileController {

    private final ZipCompressFileService zipCompressFileService;

    private static final String ZIP_MEDIA_TYPE = "application/zip";

    @Operation(summary = "Generate zip file in memory", description = "Generate zip file in memory simulation (not recommended for large files or high traffic)")
    @GetMapping(value = "/generate-in-memory", produces = ZIP_MEDIA_TYPE)
    public ResponseEntity<byte[]> generateZipInMemory(@ModelAttribute @Valid StudentsFindForm form) {
        byte[] data = zipCompressFileService.generateZipInMemory(form);
        return ResponseEntity.ok()
                .headers(headers -> headers
                        .setContentDisposition(
                                ContentDisposition.attachment().filename("export-data-in-memory.zip").build()))
                .contentType(MediaType.parseMediaType(ZIP_MEDIA_TYPE))
                .cacheControl(CacheControl.noStore().mustRevalidate())
                .body(data);
    }

    @Operation(summary = "Generate zip file in disk", description = "Generate zip file in disk simulation (recommended)")
    @GetMapping(value = "/generate-in-disk", produces = ZIP_MEDIA_TYPE)
    public ResponseEntity<StreamingResponseBody> generateZipInDisk(@ModelAttribute @Valid StudentsFindForm form) {
        StreamingResponseBody data = zipCompressFileService.generateZipInDisk(form);
        return ResponseEntity.ok()
                .headers(headers -> headers
                        .setContentDisposition(
                                ContentDisposition.attachment().filename("export-data-in-disk.zip").build()))
                .contentType(MediaType.parseMediaType(ZIP_MEDIA_TYPE))
                .cacheControl(CacheControl.noStore().mustRevalidate())
                .body(data);
    }
}
