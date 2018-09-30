package pl.wturnieju.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.validator.Validators;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService implements IUserService, ICurrentUser {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void changePassword(String username, String password) throws ValidationException {
        if (!Validators.getPasswordValidator().validate(password)) {
            throw new ValidationException("Bad email or password");
        }
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String password) throws ValidationException {
        changePassword(getCurrentUser().getUsername(), password);
    }

    @Override
    public boolean checkCredentials(String email, String password) {
        log.info("Checking user: {}", email);

        return userRepository.findByUsername(email)
                .map(User::getPassword)
                .map(encodedPass -> passwordEncoder.matches(password, encodedPass))
                .orElse(false);
    }

    @Override
    public void changeEmail(String username, String password) throws ValidationException {
        if (!Validators.getEmailValidator().validate(username)) {
            throw new ValidationException("Bad email format");
        }
        if (userRepository.findAllByUsername(username).size() == 1) {
            throw new IllegalArgumentException("Email exists");
        }
        Optional.ofNullable(getCurrentUser()).ifPresent(user -> {
            if (!checkCredentials(user.getUsername(), password)) {
                throw new IllegalArgumentException("Invalid password");
            }
            user.setUsername(username);
            userRepository.save(user);
            SecurityContextHolder.clearContext();

        });
    }

    @Override
    public void create(String username, String password) throws ValidationException {
        if (!Validators.getEmailValidator().validate(username) ||
                !Validators.getPasswordValidator().validate(password)) {
            throw new ValidationException("Bad email or password");
        }
        if (checkIfUserExists(username)) {
            throw new IllegalArgumentException("User already exists: " + username);
        }
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
        log.info("New user has been creates {}", username);
    }

    private boolean checkIfUserExists(String username) {
        if (username == null) {
            return false;
        }
        return userRepository.findByUsername(username).isPresent();
    }


    @Override
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElse(null);
    }


}
