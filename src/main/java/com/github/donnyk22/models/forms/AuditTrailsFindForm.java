package com.github.donnyk22.models.forms;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.donnyk22.models.enums.Action;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AuditTrailsFindForm extends SearchForm {
    private Integer userId;
    private Integer dataId;
    @Schema(allowableValues = { "mst_attendances", "mst_classes", "mst_homeroom_teachers", "mst_students",
            "mst_teachers", "mst_users" })
    private String table;
    @Schema(implementation = Action.class, allowableValues = { "POST", "PUT", "PATCH", "DELETE" })
    private String method;
    @NotNull(message = "Start Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Schema(example = "2026-03-08T09:00:00+07:00", description = "Start Date with Timezone")
    private OffsetDateTime startDateTime;

    @NotNull(message = "End Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Schema(example = "2026-03-08T18:00:00+07:00", description = "End Date with Timezone")
    private OffsetDateTime endDateTime;
}
