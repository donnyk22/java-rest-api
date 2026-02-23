package com.github.donnyk22.services.auth;

import java.util.concurrent.TimeUnit;

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
import com.github.donnyk22.utils.GeneralUtil;
import com.github.donnyk22.utils.JwtUtil;
import com.github.donnyk22.utils.RedisTokenUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final RedisTokenUtil redisTokenUtil;
    private final AuthUtil authUtil;

    @Override
    public UsersDto register(UserRegisterForm form, HttpServletRequest httpRequest) {
        String userIp = GeneralUtil.getClientIp(httpRequest);

        if(usersRepository.findByEmail(form.getEmail()) != null){
            throw new ConflictException("Email already exist");
        }
        if(usersRepository.findByUsername(form.getUsername()) != null){
            throw new ConflictException("Username already exist");
        }
        
        if(!form.getPassword().equals(form.getRePassword())){
            throw new BadRequestException("Retype password doesn't match. Please try again!");
        }
        Users user = UsersMapper.toRegisterEntity(form, new BCryptPasswordEncoder().encode(form.getPassword()));
        if(user == null){
            throw new BadRequestException("Failed to register a new user. Please try again");
        }

        bruteForceProtection("register", userIp);

        usersRepository.save(user);
        return UsersMapper.toBaseDto(user);
    }

    @Override
    public UsersDto login(UserLoginForm form, HttpServletRequest httpRequest) {
        String userIp = GeneralUtil.getClientIp(httpRequest);

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

        checkAttempts("login", userIp);
        checkAttempts("login", form.getUsername());

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
        String header = request.getHeader("Authorization");
        if(header != null) {
            String token;
            if(header.startsWith("Bearer ")){
                token = header.substring(7);
            }else{
                token = header;
            }
            if(redisTokenUtil.isTokenValid(token)){
                redisTokenUtil.deleteToken(token, authUtil.getUserEmail());
            }
        }
        return true;
    }

    private UsersDto refreshToken(Users user) {
        redisTokenUtil.deleteTokenByEmail(user.getEmail());

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        redisTokenUtil.storeToken(token, user.getEmail());
        
        Claims claims = jwtUtil.extractClaims(token);
        return UsersMapper.toBaseDto(user).setToken(token)
            .setIssuedAt(claims.getIssuedAt().toInstant())
            .setExpiresAt(claims.getExpiration().toInstant());
    }
    
    private void bruteForceProtection(String type, String identifier) {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        Integer ttl = 10;

        if (type.equals("register")){
            timeUnit = TimeUnit.DAYS;
            ttl = 1;
        }

        String value = redisTokenUtil.get(type, identifier);

        if(value == null){
            redisTokenUtil.store(type, identifier, "1", ttl, timeUnit);
            return;
        }

        if (Integer.parseInt(value) >= 10){
            throw new BadRequestException(getErrorMsg(type));
        }

        Integer attempts = Integer.parseInt(value) + 1;
        redisTokenUtil.updateKeepTTL(type, identifier, attempts.toString());
    }

    private void checkAttempts(String type, String identifier) {
        String value = redisTokenUtil.get(type, identifier);
        Integer attempts = value == null ? 0 : Integer.parseInt(value);

        if (attempts >= 10){
            throw new BadRequestException(getErrorMsg(type));
        }
    }

    private void resetAttempts(String type, String identifier) {
        redisTokenUtil.delete(type, identifier);
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
