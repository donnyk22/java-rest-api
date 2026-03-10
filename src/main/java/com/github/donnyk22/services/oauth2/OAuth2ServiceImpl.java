package com.github.donnyk22.services.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.enums.UserRole;
import com.github.donnyk22.repositories.MstUsersRepository;
import com.github.donnyk22.services.audittrails.AuditTrailsService;
import com.github.donnyk22.services.auth.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private final MstUsersRepository usersRepository;
    private final AuthService authService;
    private final AuditTrailsService auditTrailsService;

    @Override
    public MstUsersDto OAuth2GetInfo(OAuth2User principal) {
        String email = principal.getAttribute("email");
        if (email == null) {
            throw new BadRequestException("Cannot find email information from google oauth2. Please use another account");
        }
        MstUsers user = usersRepository.findByEmail(email);
        if (user != null) {
            auditTrailsService.create(user.getId(), "Login with google oauth2 successfully");
            return authService.refreshToken(user);
        }

        String userName = principal.getAttribute("sub");
        String role = UserRole.STUDENT.name();
        
        user = new MstUsers()
            .setUsername(userName)
            .setEmail(email)
            .setRole(role);
        user = usersRepository.saveAndFlush(user);

        auditTrailsService.create(user.getId(), "User registered with google oauth2");
        return authService.refreshToken(user);
    }
    
}
