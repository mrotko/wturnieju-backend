package pl.wturnieju.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.User;

public interface IUserService extends UserDetailsService, ICurrentUserProvider {

    void changePassword(String username, String password) throws ValidationException;

    void changePassword(String password) throws ValidationException;

    boolean checkCredentials(String email, String password);

    void changeEmail(String username, String password) throws ValidationException;

    void create(String username, String password) throws ValidationException;

    Optional<User> getById(String userId);

    Optional<User> getByProfile(IProfile profile);
}
