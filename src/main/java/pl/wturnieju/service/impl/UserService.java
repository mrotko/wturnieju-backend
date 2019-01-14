package pl.wturnieju.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.wturnieju.exception.IncorrectPasswordException;
import pl.wturnieju.exception.InvalidFormatException;
import pl.wturnieju.exception.ResourceExistsException;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.User;
import pl.wturnieju.model.UserGrantedAuthority;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.IUserService;
import pl.wturnieju.validator.Validators;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void changePassword(String username, String newPassword, String oldPassword) {
        validatePasswordFormat(newPassword);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        validateCredentials(user, oldPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    private void validatePasswordFormat(String password) {
        try {
            Validators.getPasswordValidator().validateAndThrowInvalid(password);
        } catch (ValidationException e) {
            throw new InvalidFormatException(e.getMessage());
        }
    }

    @Override
    public void changePassword(String newPassword, String oldPassword) {
        changePassword(getCurrentUser().getUsername(), newPassword, oldPassword);
    }

    @Override
    public boolean checkCredentials(String email, String rawPassword) {
        log.info("Checking user: {}", email);

        return userRepository.findByUsername(email)
                .map(User::getPassword)
                .map(encodedPass -> passwordEncoder.matches(rawPassword, encodedPass))
                .orElse(false);
    }

    @Override
    public void validateEmailChange(String username, String password) {
        validateEmailFormat(username);
        validateThatUserNotExists(username);

        var user = Optional.ofNullable(getCurrentUser()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found"));
        validateCredentials(user, password);
    }

    @Override
    public boolean isAccountActive(String email) {
        return userRepository.findByUsername(email)
                .map(User::isEnabled)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
    }

    @Override
    public void resetPassword(String email, String password) {
        validatePasswordFormat(password);
        userRepository.findByUsername(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        });
    }

    private void validateCredentials(@NonNull User user, @NonNull String rawPassword) {
        if (!checkCredentials(user.getUsername(), rawPassword)) {
            throw new IncorrectPasswordException("Incorrect password");
        }
    }

    private void validateEmailFormat(String email) {
        try {
            Validators.getEmailValidator().validateAndThrowInvalid(email);
        } catch (ValidationException e) {
            throw new InvalidFormatException(e.getMessage());
        }
    }

    private void validateThatUserNotExists(String email) {
        if (checkIfUserExists(email)) {
            throw new ResourceExistsException("Email exists");
        }
    }

    @Override
    public void setName(String name) {
        var user = getCurrentUser();
        user.setName(name);
        userRepository.save(user);
    }

    @Override
    public void setSurname(String surname) {
        var user = getCurrentUser();
        user.setSurname(surname);
        userRepository.save(user);
    }

    @Override
    public void create(String username, String password) {
        validateEmailFormat(username);
        validatePasswordFormat(password);
        validateThatUserNotExists(username);

        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
        log.info("New user has been creates {}", username);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User getUserById(String userId) {
        return findUserById(userId).orElse(null);
    }

    @Override
    public Optional<User> findUseByProfile(IProfile profile) {
        if (profile == null) {
            return Optional.empty();
        }
        return findUserById(profile.getId());
    }

    @Override
    public User getUserByProfile(IProfile profile) {
        return findUseByProfile(profile).orElse(null);
    }

    @Override
    public void setAuthorities(Collection<UserGrantedAuthority> authorities) {
        var user = getCurrentUser();
        user.setAuthorities(new HashSet<>(authorities));
        userRepository.save(user);
    }

    @Override
    public void confirmNewAccount(String email) {
        userRepository.findByUsername(email).ifPresent(user -> {
            user.setEnabled(true);
            userRepository.save(user);
        });
    }

    @Override
    public void confirmChangedEmail(String email, String newEmail) {
        userRepository.findByUsername(email).ifPresent(user -> {
            user.setUsername(newEmail);
            userRepository.save(user);
        });
    }

    private boolean checkIfUserExists(@NonNull String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    @Override
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElse(null);
    }
}
