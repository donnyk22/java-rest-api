package com.github.donnyk22.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.donnyk22.models.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
    Users findByEmail(String email);
    Users findByUsername(String username);
}
