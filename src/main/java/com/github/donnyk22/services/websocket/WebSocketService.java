package com.github.donnyk22.services.websocket;

import com.github.donnyk22.models.dtos.WebSocketUserSessionDto;
import com.github.donnyk22.models.forms.WebSocketForm;
import com.github.donnyk22.models.forms.WebSocketUsersForm;

public interface WebSocketService {
    WebSocketForm sendMessages(WebSocketForm message);
    WebSocketForm sendMessagesToUsers(WebSocketUsersForm message);
    WebSocketUserSessionDto getActiveUsers();
}
