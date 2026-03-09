package com.github.donnyk22.services.audittrails;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.LogAuditTrailsDto;
import com.github.donnyk22.models.entities.LogAuditTrails;

import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.enums.Method;
import com.github.donnyk22.models.forms.AuditTrailsFindForm;
import com.github.donnyk22.models.mappers.LogAuditTrailsMapper;
import com.github.donnyk22.repositories.LogAuditTrailsRepository;
import com.github.donnyk22.utils.AuthUtil;
import com.github.donnyk22.utils.ConverterUtil;
import com.github.donnyk22.utils.EntityUtil;

import lombok.RequiredArgsConstructor;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditTrailsServiceImpl implements AuditTrailsService {

    private final LogAuditTrailsRepository auditTrailsRepository;
    private final AuthUtil authUtil;
    private final ConverterUtil converterUtil;

    @Override
    public FindResponse<LogAuditTrailsDto> findAuditTrails(AuditTrailsFindForm form) {
        Pageable pageable = PageRequest.of(form.getPage(), form.getSize());
        Specification<LogAuditTrails> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<LogAuditTrails, MstUsers> userJoin = root.join("userData", JoinType.LEFT);

            if (StringUtils.hasText(form.getKeyword())) {
                String likePattern = "%" + form.getKeyword().toLowerCase() + "%";
                Predicate predicate = cb.or(
                    cb.like(cb.lower(root.get("details")), likePattern),
                    cb.like(cb.lower(userJoin.get("username")), likePattern),
                    cb.like(cb.lower(userJoin.get("email")), likePattern)
                );
                predicates.add(predicate);
            }

            if (form.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId"), form.getUserId()));
            }

            if (form.getDataId() != null) {
                predicates.add(cb.equal(root.get("dataId"), form.getUserId()));
            }

            if (StringUtils.hasText(form.getTable())) {
                predicates.add(cb.equal(root.get("table"), form.getTable()));
            }

            if (StringUtils.hasText(form.getMethod())) {
                predicates.add(cb.equal(root.get("method"), form.getMethod()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<LogAuditTrails> result = auditTrailsRepository.findAll(spec, pageable);
        return new FindResponse<LogAuditTrailsDto>()
            .setRecords(result.getContent().stream().map(LogAuditTrailsMapper::toDto).toList())
            .setTotalPage(result.getTotalPages())
            .setTotalItem((int) result.getTotalElements())
            .setHasNext(result.hasNext())
            .setHasPrev(result.hasPrevious());
    }

    @Override
    public <T> void create(Method method, T data, String details) {
        try {
            java.lang.reflect.Method setPasswordMethod = data.getClass().getMethod("setPassword", String.class);
            setPasswordMethod.invoke(data, (Object) null);
        } catch (Exception e) {
            // ignore if dont have password property
        }

        LogAuditTrails auditTrails = new LogAuditTrails()
            .setUserId(authUtil.getUserId())
            .setMethod(method == null ? null : method.name())
            .setTable(EntityUtil.getTableName(data))
            .setDetails(details)
            .setDataId(EntityUtil.getIdByReflection(data))
            .setProperties(converterUtil.genericToJson(data));
        auditTrailsRepository.save(auditTrails);
    }

    @Override
    public void create(Integer userId, String details) {
        LogAuditTrails auditTrails = new LogAuditTrails()
            .setUserId(userId)
            .setDetails(details);
        auditTrailsRepository.save(auditTrails);
    }
    
}
