package pl.wturnieju.service.impl;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.wturnieju.service.IEmailService;

@Component
@RequiredArgsConstructor
@Log4j2
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        log.debug("Sending email to " + to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailUsername);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        Executors.newSingleThreadExecutor().execute(() -> javaMailSender.send(message));
    }
}
