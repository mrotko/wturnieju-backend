package pl.wturnieju.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.wturnieju.model.User;

public interface IUserService extends UserDetailsService {

    void create(User user);

    void resetPassword(String username, String password);

    void resetPassword(String password);

    boolean check(String email, String password);

    void changeEmail(String email);
}
