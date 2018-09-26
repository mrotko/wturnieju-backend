package pl.wturnieju.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.wturnieju.exception.ValidationException;

public interface IUserService extends UserDetailsService {

    void changePassword(String username, String password) throws ValidationException;

    void changePassword(String password) throws ValidationException;

    boolean checkCredentials(String email, String password);

    void changeEmail(String email);

    void create(String username, String password) throws ValidationException;
}
