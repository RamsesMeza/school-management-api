package com.school.management.api.email;

import com.school.management.api.auth.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@AllArgsConstructor
@Service
public class EmailAuthService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendVerificationUser(User user, String link) {

        try {
            Context context = new Context();
            context.setVariable("name", user.getFullName());
            context.setVariable("link", link);
            context.setVariable("hours", 24);

            String html = templateEngine.process("emails/verification-email", context);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Verify your account");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    public void sendRecoverPasswordEmail(User user, String link) {

        try {
            Context context = new Context();
            context.setVariable("name", user.getFullName());
            context.setVariable("link", link);
            context.setVariable("hours", 24);

            String html = templateEngine.process("emails/recover-password-email", context);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Recover password");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
