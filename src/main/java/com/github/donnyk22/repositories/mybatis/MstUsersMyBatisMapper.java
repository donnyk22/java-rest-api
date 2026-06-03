package com.github.donnyk22.repositories.mybatis;

import java.time.OffsetDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;

@Mapper
public interface MstUsersMyBatisMapper {
    List<MstUsersDto> search(@Param("keyword") String keyword,
            @Param("role") String role,
            @Param("isActive") Boolean isActive,
            @Param("offset") int offset,
            @Param("size") int size);

    long countSearch(@Param("keyword") String keyword,
            @Param("role") String role,
            @Param("isActive") Boolean isActive);

    MstUsersDto findById(@Param("id") Integer id);

    // === Write operations (replacing JPA MstUsersRepository) ===

    // Full entity (incl. version/password) used for optimistic locking & audit payload.
    MstUsers findEntityById(@Param("id") Integer id);

    // Conflict checks. Only consider non-deleted rows, matching JPA @SoftDelete behavior.
    Integer findIdByEmail(@Param("email") String email);

    Integer findIdByUsername(@Param("username") String username);

    // Populates the generated id back onto the passed entity (useGeneratedKeys).
    int insert(MstUsers user);

    // Optimistic-locked update: matches on current version and increments it.
    int update(MstUsers user);

    int updatePassword(@Param("id") Integer id,
            @Param("password") String password,
            @Param("updatedAt") OffsetDateTime updatedAt);

    // Soft delete to mirror Hibernate @SoftDelete (sets deleted = true).
    int softDelete(@Param("id") Integer id);
}
