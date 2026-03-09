package com.github.donnyk22.models.dtos;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class LogAuditTrailsDto {
    private Integer id;
    private MstUsersDto user;
    private String action;
    private String table;
    private String details;
    private Integer dataId;
    private String properties;
    private OffsetDateTime createdAt;
}
