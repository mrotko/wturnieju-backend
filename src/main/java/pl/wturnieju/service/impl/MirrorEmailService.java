package pl.wturnieju.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Profile("dev")
@Log4j2
public class MirrorEmailService extends EmailService {

    public MirrorEmailService(JavaMailSender javaMailSender) {
        super(javaMailSender);
        log.warn("Loaded mirror email service");
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        super.sendSimpleMessage(emailUsername, subject, text);
    }
}
