package com.github.donnyk22.models.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationLetterForm {
    @NotBlank(message = "Applicant name is required")
    @Schema(example = "John Doe", defaultValue = "John Doe")
    private String applicantName;

    @NotBlank(message = "Applicant address is required")
    @Schema(example = "Jakarta, Indonesia", defaultValue = "Jakarta, Indonesia")
    private String applicantAddress;

    @NotBlank(message = "Applicant phone is required")
    @Schema(example = "+628123456789", defaultValue = "+628123456789")
    private String applicantPhone;

    @NotBlank(message = "Applicant email is required")
    @Schema(example = "johndoe@gmail.com", defaultValue = "johndoe@gmail.com")
    private String applicantEmail;

    @NotBlank(message = "Recipient name is required")
    @Schema(example = "Hiring Manager", defaultValue = "Hiring Manager")
    private String recipientName;

    @NotBlank(message = "Recipient title is required")
    @Schema(example = "Human Resources Manager", defaultValue = "Human Resources Manager")
    private String recipientTitle;

    @NotBlank(message = "Company name is required")
    @Schema(example = "PT Gudang Garam Tbk", defaultValue = "PT Gudang Garam Tbk")
    private String recipientCompany;

    @NotBlank(message = "Source media is required")
    @Schema(example = "LinkedIn", defaultValue = "LinkedIn")
    private String sourceMedia;

    @NotBlank(message = "Target position is required")
    @Schema(example = "Software Engineer", defaultValue = "Software Engineer")
    private String targetPosition;
}
