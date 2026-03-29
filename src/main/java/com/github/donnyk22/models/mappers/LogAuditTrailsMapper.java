package com.github.donnyk22.models.mappers;

import com.github.donnyk22.models.dtos.LogAuditTrailsDto;
import com.github.donnyk22.models.entities.LogAuditTrails;

public class LogAuditTrailsMapper {

    public static LogAuditTrailsDto toDto(LogAuditTrails auditTrails) {
        LogAuditTrailsDto dto = new LogAuditTrailsDto()
                .setId(auditTrails.getId())
                .setUser(MstUsersMapper.toBaseDto(auditTrails.getUserData()))
                .setAction(auditTrails.getMethod())
                .setTable(auditTrails.getTable())
                .setDetails(auditTrails.getDetails())
                .setDataId(auditTrails.getDataId())
                .setProperties(auditTrails.getProperties())
                .setCreatedAt(auditTrails.getCreatedAt());
        return dto;
    }

}
