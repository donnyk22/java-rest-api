package com.github.donnyk22.services.mfa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.exceptions.ResourceNotFoundException;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.mappers.MstUsersMapper;
import com.github.donnyk22.repositories.MstUsersRepository;
import com.github.donnyk22.services.audittrails.AuditTrailsService;
import com.github.donnyk22.utils.AuthUtil;
import com.github.donnyk22.utils.JwtUtil;
import com.github.donnyk22.utils.RedisUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService{

    private final MstUsersRepository usersRepository;
    private final AuditTrailsService auditTrailsService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final AuthUtil authUtil;

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Override
    public MstUsersDto loginMfa(UserLoginForm form) {
        MstUsers user = usersRepository.findByEmail(form.getUsername());
        if(user == null){
            user = usersRepository.findByUsername(form.getUsername());
            if(user == null){
                throw new ResourceNotFoundException("User not found");
            }
        }
        Boolean passwordMatch = new BCryptPasswordEncoder().matches(form.getPassword(), user.getPassword());
        if(!passwordMatch){
            auditTrailsService.create(user.getId(), "Login failed");
            throw new BadRequestException("Invalid email or password");
        }

        String detailsMsg = "Login successfully";
        if (user.getMfaEnabled()) {
            detailsMsg = "Login with MFA activated successfully";
        }
        auditTrailsService.create(user.getId(), detailsMsg);
        return refreshToken(user, true);
    }

    @Override
    public MstUsersDto verifyMfa(String code) {
        MstUsers user = usersRepository.findById(authUtil.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authUtil.getUserId()));

        if (verifyCode(user.getMfaSecret(), code)) {
            return refreshToken(user, false);
        }

        throw new BadRequestException("Invalid code");
    }

    @Override
    public byte[] mfaQrCodeGenerate() {
        try {
            MstUsers user = usersRepository.findById(authUtil.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authUtil.getUserId()));

            String email = user.getEmail();
            String secret = user.getMfaSecret();

            if (!StringUtils.hasText(user.getMfaSecret())) {
                secret = new DefaultSecretGenerator().generate();
                user.setMfaSecret(secret);
            }

            user.setMfaEnabled(true);
            usersRepository.save(user);
            
            String qrCodeText = "otpauth://totp/" + APP_NAME + ":" + email + "?secret=" + secret + "&issuer=" + APP_NAME;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 500, 500);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (WriterException e) {
            throw new BadRequestException("Failed to convert QR Code: " + e.getMessage());
        } catch (IOException e) {
            throw new BadRequestException("Failed to write QR Code into file: " + e.getMessage());
        }
    }

    private MstUsersDto refreshToken(MstUsers user, Boolean isMfaToken) {
        redisUtil.deleteToken(authUtil.getUserEmail(), authUtil.getSessionId());
        String sessionId = UUID.randomUUID().toString();
        String token = jwtUtil.generateToken(user, sessionId);
        if (isMfaToken && user.getMfaEnabled()) {
            token = jwtUtil.generateMfaToken(user, sessionId);
        }
        redisUtil.storeToken(token, user.getEmail(), sessionId);
        
        Claims claims = jwtUtil.extractClaims(token);
        return MstUsersMapper.toBaseDto(user).setToken(token)
            .setIssuedAt(claims.getIssuedAt().toInstant())
            .setExpiresAt(claims.getExpiration().toInstant());
    }

    private boolean verifyCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return verifier.isValidCode(secret, code);
    }
}
