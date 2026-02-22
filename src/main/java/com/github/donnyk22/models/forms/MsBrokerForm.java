package com.github.donnyk22.models.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MsBrokerForm {
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "message is required")
    private String message;
}
