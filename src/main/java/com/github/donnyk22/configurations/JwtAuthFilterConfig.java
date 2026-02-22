package com.github.donnyk22.configurations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.utils.JwtUtil;
import com.github.donnyk22.utils.RedisTokenUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilterConfig extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final RedisTokenUtil redisTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");
        if(header != null) {
            String token;
            if(header.startsWith("Bearer ")){
                token = header.substring(7);
            }else{
                token = header;
            }

            if(!redisTokenUtil.isTokenValid(token)){
                sendUnauthorizedResponse(res, "Token expired or invalid");
                return;
            }

            Claims claims = jwtUtil.extractClaims(token);
            Integer id = Integer.valueOf(claims.getSubject());
            String name = claims.get("username", String.class);
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())); // To handle @PreAuthorize("hasRole('ADMIN')") and @PreAuthorize("hasAnyRole('ROLE_ADMIN')") in controller, we need to prefix role with "ROLE_"

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(id, null, authorities);

            auth.setDetails(Map.of(
                "username", name,
                "email", email,
                "role", role
            ));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(req, res);
    }

    private void sendUnauthorizedResponse(HttpServletResponse res, String errorMessage) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        ApiResponse<Object> response = new ApiResponse<>(
            HttpServletResponse.SC_UNAUTHORIZED,
            errorMessage,
            null
        );
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        res.getWriter().write(json);
    }
}
