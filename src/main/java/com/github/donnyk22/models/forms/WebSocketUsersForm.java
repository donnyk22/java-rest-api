package com.github.donnyk22.models.forms;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebSocketUsersForm extends WebSocketForm {
    @NotNull(message = "User IDs must not be null")
    @Size(min = 1, message = "User IDs must contain at least one user")
    private List<Integer> userIds;
}
