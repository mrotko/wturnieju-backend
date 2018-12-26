package pl.wturnieju.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.User;
import pl.wturnieju.model.UserGrantedAuthority;

public interface IUserService extends UserDetailsService, ICurrentUserProvider {

    void changePassword(String username, String newPassword, String oldPassword);

    void changePassword(String newPassword, String oldPassword);

    boolean checkCredentials(String email, String password);

    void validateEmailChange(String username, String password);

    void setName(String name);

    void setSurname(String surname);

    void create(String username, String password);

    Optional<User> getById(String userId);

    Optional<User> getByProfile(IProfile profile);

    void setAuthorities(Collection<UserGrantedAuthority> authorities);

    void confirmNewAccount(String email);

    void confirmChangedEmail(String email, String newEmail);
}
