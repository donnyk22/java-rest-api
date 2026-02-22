package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.Students;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Integer>, JpaSpecificationExecutor<Students> {
    
}
