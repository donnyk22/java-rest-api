package com.github.donnyk22.services.websocket;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import com.github.donnyk22.models.dtos.WebSocketUserSessionDetailDto;
import com.github.donnyk22.models.dtos.WebSocketUserSessionDto;
import com.github.donnyk22.models.forms.WebSocketForm;
import com.github.donnyk22.models.forms.WebSocketUsersForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebsocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private final SimpUserRegistry simpUserRegistry;

    @Override
    public WebSocketForm sendMessages(WebSocketForm message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
        return message;
    }

    @Override
    public WebSocketForm sendMessagesToUsers(WebSocketUsersForm message) {
        for (Integer userId: message.getUserIds()) {
            messagingTemplate.convertAndSendToUser(
                Integer.toString(userId),
                "/queue/messages",
                new WebSocketForm(message.getSubject(), message.getContent())
            );
        }
        return new WebSocketForm(message.getSubject(), message.getContent());
    }

    @Override
    public WebSocketUserSessionDto getActiveUsers() {
        List<WebSocketUserSessionDetailDto> users = simpUserRegistry.getUsers().stream()
            .map(user -> new WebSocketUserSessionDetailDto(
                user.getName(),
                user.getSessions()
                    .stream()
                    .map(SimpSession::getId)
                    .collect(Collectors.toSet())
            ))
            .collect(Collectors.toList());

        WebSocketUserSessionDto result = new WebSocketUserSessionDto()
            .setCount(simpUserRegistry.getUserCount())
            .setDetail(users);

        return result;
    }
    
}
