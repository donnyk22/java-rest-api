package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.donnyk22.models.entities.MstStudents;

public interface MstStudentsRepository
        extends JpaRepository<MstStudents, Integer>, JpaSpecificationExecutor<MstStudents> {

}
