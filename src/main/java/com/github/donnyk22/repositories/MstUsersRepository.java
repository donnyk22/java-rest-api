package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.donnyk22.models.entities.MstUsers;

public interface MstUsersRepository extends JpaRepository<MstUsers, Integer>, JpaSpecificationExecutor<MstUsers> {
    MstUsers findByEmail(String email);

    MstUsers findByUsername(String username);
}
