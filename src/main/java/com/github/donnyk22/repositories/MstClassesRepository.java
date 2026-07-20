package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.donnyk22.models.entities.MstClasses;

public interface MstClassesRepository extends JpaRepository<MstClasses, Integer>, JpaSpecificationExecutor<MstClasses> {

}
