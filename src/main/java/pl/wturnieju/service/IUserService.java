package pl.wturnieju.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.wturnieju.model.User;

public interface IUserService extends UserDetailsService {

    User create(User user);

    User resetPassword(String password);
}
