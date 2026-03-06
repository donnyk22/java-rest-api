package com.github.donnyk22.services.email;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.github.donnyk22.models.entities.Users;
import com.github.donnyk22.models.forms.EmailForm;
import com.github.donnyk22.repositories.UsersRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UsersRepository usersRepository;

    @Value("${app.email.sender}")
    private String SENDER;

    @Override
    @Async //implement async function
    public CompletableFuture<List<String>> sendEmailSimple(EmailForm form) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            
            String[] recipientArray = form.getRecipients().toArray(new String[0]);
            message.setTo(recipientArray);
            message.setSubject(form.getSubject());
            message.setText(form.getMessage());
            message.setFrom(SENDER);

            mailSender.send(message);
            return CompletableFuture.completedFuture(form.getRecipients());
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async //implement async function
    public CompletableFuture<List<String>> sendEmail(EmailForm form) {
        try{
            for (String to : form.getRecipients()) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                String userName = to;
                Boolean isRegisteredUser = false;
                Users user = usersRepository.findByEmail(to);
                if (user != null) {
                    if (user.getStudentData() != null) {
                        userName = user.getStudentData().getFullName();
                    } else if (user.getTeacherData() != null) {
                        userName = user.getTeacherData().getFullName();
                    } else {
                        userName = user.getUsername();
                    }
                    isRegisteredUser = true;
                }
                List<String> items = List.of("Item 1", "Item 2", "Item 3");

                Context context = new Context();
                context.setVariable("userName", userName);
                context.setVariable("isRegisteredUser", isRegisteredUser);
                context.setVariable("messageBody", form.getMessage());
                context.setVariable("items", items);

                String htmlContent = templateEngine.process("email-template", context);

                helper.setTo(to); //use array to send to multiple recipients at once
                helper.setSubject(form.getSubject());
                helper.setText(htmlContent, true);
                helper.setFrom(SENDER);

                ClassPathResource imageResource = new ClassPathResource("images/company-logo.jpg");
                helper.addInline("logoImage", imageResource); 

                mailSender.send(message);
            }
            
            return CompletableFuture.completedFuture(form.getRecipients());
        } catch (MessagingException e) {
            log.error("Error to send the message: ", e);
            return CompletableFuture.failedFuture(e);
        } catch (Exception e) {
            log.error("Unexpected error sending email: ", e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
}
