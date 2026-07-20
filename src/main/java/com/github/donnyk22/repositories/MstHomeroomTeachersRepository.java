package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.donnyk22.models.entities.MstHomeroomTeachers;

public interface MstHomeroomTeachersRepository
        extends JpaRepository<MstHomeroomTeachers, Integer>, JpaSpecificationExecutor<MstHomeroomTeachers> {

    MstHomeroomTeachers findByClassIdAndTeacherId(Integer classId, Integer teacherId);

}
