package com.github.donnyk22.services.mybatis;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ConflictException;
import com.github.donnyk22.exceptions.ResourceNotFoundException;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.enums.Action;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersFindForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;
import com.github.donnyk22.models.mappers.MstUsersMapper;
import com.github.donnyk22.repositories.mybatis.MstUsersMyBatisMapper;
import com.github.donnyk22.services.audittrails.AuditTrailsService;
import com.github.donnyk22.utils.EntityUtil;
import com.github.donnyk22.utils.MediaUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBatisServiceImpl implements MyBatisService {

    private final MstUsersMyBatisMapper usersMyBatisMapper;
    private final AuditTrailsService auditTrailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MediaUtil mediaUtil;

    // === Users ===

    @Override
    @Transactional(readOnly = true)
    public FindResponse<MstUsersDto> findUsers(UsersFindForm form) {
        int page = form.getPage();
        int size = form.getSize();
        int offset = page * size;

        List<MstUsersDto> records = usersMyBatisMapper.search(
                form.getKeyword(), form.getRole(), form.getIsActive(), offset, size);
        long totalItem = usersMyBatisMapper.countSearch(
                form.getKeyword(), form.getRole(), form.getIsActive());
        int totalPage = size == 0 ? 0 : (int) Math.ceil((double) totalItem / size);

        return new FindResponse<MstUsersDto>()
                .setRecords(records)
                .setTotalPage(totalPage)
                .setTotalItem((int) totalItem)
                .setHasNext(page + 1 < totalPage)
                .setHasPrev(page > 0);
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    @Transactional(readOnly = true)
    public MstUsersDto readUser(Integer id) {
        MstUsersDto user = usersMyBatisMapper.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        return user;
    }

    @Override
    @Transactional
    public MstUsersDto createUser(UsersCreateForm form) {
        if (!form.getPassword().equals(form.getRePassword())) {
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        if (usersMyBatisMapper.findIdByEmail(form.getEmail()) != null) {
            throw new ConflictException("Email already exist");
        }
        if (usersMyBatisMapper.findIdByUsername(form.getUsername()) != null) {
            throw new ConflictException("Username already exist");
        }
        String photo;
        try {
            photo = mediaUtil.toBase64(form.getPhoto());
        } catch (Exception e) {
            throw new BadRequestException("Invalid photo format: " + e.getMessage(), e);
        }
        MstUsers user = MstUsersMapper.toEntity(form, photo, passwordEncoder.encode(form.getPassword()));
        // Timestamps & version were previously managed by JPA lifecycle/@Version; set
        // them manually now.
        OffsetDateTime now = OffsetDateTime.now();
        user.setVersion(0);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        usersMyBatisMapper.insert(user); // useGeneratedKeys populates user.id
        auditTrailsService.create(Action.POST, user, "Create user record");
        return MstUsersMapper.toBaseDto(user);
    }

    @Override
    @CachePut(value = "user", key = "#id")
    @Transactional
    public MstUsersDto updateUser(Integer id, UsersUpdateForm form) {
        MstUsers user = usersMyBatisMapper.findEntityById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        EntityUtil.compareVersion(user.getVersion(), form.getVersion());
        Integer existingEmailId = usersMyBatisMapper.findIdByEmail(form.getEmail());
        if (existingEmailId != null && !existingEmailId.equals(user.getId())) {
            throw new ConflictException("Email already exist");
        }
        Integer existingUsernameId = usersMyBatisMapper.findIdByUsername(form.getUsername());
        if (existingUsernameId != null && !existingUsernameId.equals(user.getId())) {
            throw new ConflictException("Username already exist");
        }
        String photo;
        try {
            photo = mediaUtil.toBase64(form.getPhoto());
        } catch (Exception e) {
            throw new BadRequestException("Invalid photo format: " + e.getMessage(), e);
        }
        Integer currentVersion = user.getVersion();
        user = MstUsersMapper.toEntity(user, form, photo);
        user.setUpdatedAt(OffsetDateTime.now());
        // update() matches WHERE version = currentVersion; 0 rows means a concurrent
        // change.
        if (usersMyBatisMapper.update(user) == 0) {
            throw new ConflictException("Data is already updated by another user. Please refresh and try again.");
        }
        user.setVersion(currentVersion + 1); // reflect the bumped version in the response/audit
        auditTrailsService.create(Action.PUT, user, "Update user record");
        return MstUsersMapper.toBaseDto(user);
    }

    @Override
    @Transactional
    public MstUsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form) {
        if (!form.getPassword().equals(form.getRePassword())) {
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        MstUsers user = usersMyBatisMapper.findEntityById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        OffsetDateTime now = OffsetDateTime.now();
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        usersMyBatisMapper.updatePassword(id, encodedPassword, now);
        user.setPassword(encodedPassword);
        user.setUpdatedAt(now);
        user.setVersion(user.getVersion() + 1);
        auditTrailsService.create(Action.PATCH, user, "Update user password");
        return MstUsersMapper.toBaseDto(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public MstUsersDto deleteUser(Integer id) {
        MstUsers user = usersMyBatisMapper.findEntityById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        usersMyBatisMapper.softDelete(id); // soft delete, mirroring Hibernate @SoftDelete
        auditTrailsService.create(Action.DELETE, user, "Delete user record");
        return MstUsersMapper.toBaseDto(user);
    }

}