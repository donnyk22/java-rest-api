package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.MstHomeroomTeachers;

@Repository
public interface MstHomeroomTeachersRepository extends JpaRepository<MstHomeroomTeachers, Integer>, JpaSpecificationExecutor<MstHomeroomTeachers>{
    
    MstHomeroomTeachers findByClassIdAndTeacherId(Integer classId, Integer teacherId);

}
