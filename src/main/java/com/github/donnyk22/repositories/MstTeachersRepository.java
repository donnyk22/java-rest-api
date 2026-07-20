package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.donnyk22.models.entities.MstTeachers;

public interface MstTeachersRepository
        extends JpaRepository<MstTeachers, Integer>, JpaSpecificationExecutor<MstTeachers> {

}
