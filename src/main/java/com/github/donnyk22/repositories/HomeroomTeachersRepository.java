package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.HomeroomTeachers;

@Repository
public interface HomeroomTeachersRepository extends JpaRepository<HomeroomTeachers, Integer>, JpaSpecificationExecutor<HomeroomTeachers>{
    
}
