package com.github.donnyk22.configurations;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.github.donnyk22.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class WebSocketAuthInterceptorConfig implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public WebSocketAuthInterceptorConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                Claims claims = jwtUtil.extractClaims(token);
                String userId = claims.getSubject();

                accessor.setUser(() -> userId);
            }
        }
        return message;
    }
}