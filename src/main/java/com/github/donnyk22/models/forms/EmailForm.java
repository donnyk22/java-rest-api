package com.github.donnyk22.models.forms;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class EmailForm {
    @Size(min = 1, message = "Must contain at least one recipient")
    List<@Email(message = "One or more email has invalid format") String> recipients;
    @Schema(example = "Subject", description = "Email subject")
    String subject;
    @NotBlank(message = "Message is required")
    @Schema(example = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, <br/>
        sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. <br/>
        Ut enim ad minim veniam, <br/>
        quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. <br/>
        Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. <br/>
        Excepteur sint occaecat cupidatat non proident, <br/>
        sunt in culpa qui officia deserunt mollit anim id est laborum.
        """,
        description = "Email message")
    String message;
}
