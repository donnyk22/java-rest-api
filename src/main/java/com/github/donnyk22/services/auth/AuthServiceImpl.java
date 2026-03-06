package com.github.donnyk22.services.auth;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ConflictException;
import com.github.donnyk22.exceptions.ResourceNotFoundException;
import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.entities.Users;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.forms.users.UserRegisterForm;
import com.github.donnyk22.models.mappers.UsersMapper;
import com.github.donnyk22.repositories.UsersRepository;
import com.github.donnyk22.utils.AuthUtil;
import com.github.donnyk22.utils.Util;
import com.github.donnyk22.utils.JwtUtil;
import com.github.donnyk22.utils.RedisUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthUtil authUtil;

    @Value("${app.login.max-req}")
    private Integer LOGIN_MAX_REQ;

    @Value("${app.login.max-req-minutes}")
    private Integer LOGIN_MAX_REQ_DURATION;

    @Value("${app.register.max-req}")
    private Integer REGISTER_MAX_REQ;

    @Value("${app.register.max-req-hours}")
    private Integer REGISTER_MAX_REQ_DURATION;

    @Override
    public UsersDto register(UserRegisterForm form, HttpServletRequest httpRequest) {
        String userIp = Util.getClientIp(httpRequest);

        if(usersRepository.findByEmail(form.getEmail()) != null){
            throw new ConflictException("Email already exist");
        }
        if(usersRepository.findByUsername(form.getUsername()) != null){
            throw new ConflictException("Username already exist");
        }
        
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        Users user = UsersMapper.toEntity(form, new BCryptPasswordEncoder().encode(form.getPassword()));
        if(user == null){
            throw new BadRequestException("Failed to register a new user. Please try again");
        }

        bruteForceProtection("register", userIp);

        usersRepository.save(user);
        return UsersMapper.toBaseDto(user);
    }

    @Override
    public UsersDto login(UserLoginForm form, HttpServletRequest httpRequest) {
        String userIp = Util.getClientIp(httpRequest);

        checkAttempts("login", userIp);
        checkAttempts("login", form.getUsername());

        Users user = usersRepository.findByEmail(form.getUsername());
        if(user == null){
            user = usersRepository.findByUsername(form.getUsername());
            if(user == null){
                bruteForceProtection("login", userIp);
                throw new ResourceNotFoundException("User not found");
            }
        }
        Boolean passwordMatch = new BCryptPasswordEncoder().matches(form.getPassword(), user.getPassword());
        if(!passwordMatch){
            bruteForceProtection("login", userIp);
            bruteForceProtection("login", form.getUsername());
            throw new BadRequestException("Invalid email or password");
        }

        resetAttempts("login", userIp);
        resetAttempts("login", form.getUsername());
        
        return refreshToken(user);
    }

    @Override
    public UsersDto refresh() {
        String email = authUtil.getUserEmail();

        Users user = usersRepository.findByEmail(email);
        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }

        return refreshToken(user);
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        redisUtil.deleteToken(authUtil.getUserEmail(), authUtil.getSessionId());
        return true;
    }

    private UsersDto refreshToken(Users user) {
        logout(null);
        String sessionId = UUID.randomUUID().toString();
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), sessionId);
        redisUtil.storeToken(token, user.getEmail(), sessionId);
        
        Claims claims = jwtUtil.extractClaims(token);
        return UsersMapper.toBaseDto(user).setToken(token)
            .setIssuedAt(claims.getIssuedAt().toInstant())
            .setExpiresAt(claims.getExpiration().toInstant());
    }
    
    private void bruteForceProtection(String type, String identifier) {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        Integer ttl = LOGIN_MAX_REQ_DURATION;
        Integer maxReq = LOGIN_MAX_REQ;

        if (type.equals("register")){
            timeUnit = TimeUnit.DAYS;
            ttl = REGISTER_MAX_REQ_DURATION;
            maxReq = REGISTER_MAX_REQ;
        }

        String value = redisUtil.get(type, identifier);

        if(value == null){
            redisUtil.store(type, identifier, "1", ttl, timeUnit);
            return;
        }

        if (Integer.parseInt(value) >= maxReq){
            throw new BadRequestException(getErrorMsg(type));
        }

        Integer attempts = Integer.parseInt(value) + 1;
        redisUtil.updateKeepTTL(type, identifier, attempts.toString());
    }

    private void checkAttempts(String type, String identifier) {
        Integer maxReq = LOGIN_MAX_REQ;

        if (type.equals("register")){
            maxReq = REGISTER_MAX_REQ;
        }

        String value = redisUtil.get(type, identifier);
        Integer attempts = value == null ? 0 : Integer.parseInt(value);

        if (attempts >= maxReq){
            throw new BadRequestException(getErrorMsg(type));
        }
    }

    private void resetAttempts(String type, String identifier) {
        redisUtil.delete(type, identifier);
    }

    private String getErrorMsg(String type) {
        String errorMsg = "Please try again later";

        if (type.equals("login")){
            errorMsg = "Too many failed login attempts. " + errorMsg;
        } else if (type.equals("register")){
            errorMsg = "Too many registration attempts. " + errorMsg;
        }

        return errorMsg;
    }
}
