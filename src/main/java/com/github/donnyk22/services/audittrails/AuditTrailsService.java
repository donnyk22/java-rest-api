package com.github.donnyk22.services.audittrails;

import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.LogAuditTrailsDto;
import com.github.donnyk22.models.enums.Method;
import com.github.donnyk22.models.forms.AuditTrailsFindForm;

public interface AuditTrailsService {
    FindResponse<LogAuditTrailsDto> findAuditTrails(AuditTrailsFindForm form);

    <T> void create(Method method, T data, String details);

    void create(Integer userId, String details);
}
