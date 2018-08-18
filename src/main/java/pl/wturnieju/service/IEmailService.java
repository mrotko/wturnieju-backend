package pl.wturnieju.service;

public interface IEmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
