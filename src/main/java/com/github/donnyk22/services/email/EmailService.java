package com.github.donnyk22.services.email;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.donnyk22.models.forms.EmailForm;

public interface EmailService {
    CompletableFuture<List<String>> sendEmailSimple(EmailForm form);
    CompletableFuture<List<String>> sendEmail(EmailForm form);
}
