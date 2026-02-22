package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.Teachers;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, Integer>, JpaSpecificationExecutor<Teachers> {
    
}
