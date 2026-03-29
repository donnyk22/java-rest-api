package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.LogAuditTrailsDto;
import com.github.donnyk22.models.forms.AuditTrailsFindForm;
import com.github.donnyk22.services.audittrails.AuditTrailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Audit Trail APIs", description = "Find and retrieve audit trail (manual) data")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/audit-trail")
public class AuditTrailsController {

    private final AuditTrailsService auditTrailsService;

    @Operation(summary = "Find audit trail records", description = "Retrieve and search audit trail records")
    @GetMapping()
    public ResponseEntity<ApiResponse<FindResponse<LogAuditTrailsDto>>> findAuditTrails(
            @ModelAttribute @Valid AuditTrailsFindForm form) {
        FindResponse<LogAuditTrailsDto> result = auditTrailsService.findAuditTrails(form);
        ApiResponse<FindResponse<LogAuditTrailsDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Audit trail records retrieved successfully",
                result);
        return ResponseEntity.ok(response);
    }
}
