package com.github.donnyk22.models.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketForm {
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "Content is required")
    private String content;
}
