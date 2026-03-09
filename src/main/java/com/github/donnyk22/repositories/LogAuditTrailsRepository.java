package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.LogAuditTrails;

@Repository
public interface LogAuditTrailsRepository extends JpaRepository<LogAuditTrails, Integer>, JpaSpecificationExecutor<LogAuditTrails> {
    
}
