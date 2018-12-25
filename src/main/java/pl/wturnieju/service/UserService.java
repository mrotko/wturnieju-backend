package pl.wturnieju.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

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
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.User;
import pl.wturnieju.model.UserGrantedAuthority;
import pl.wturnieju.repository.UserRepository;
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
        if (!Validators.getPasswordValidator().validate(newPassword)) {
            throw new InvalidFormatException("Invalid password format");
        }
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String newPassword, String oldPassword) {
        changePassword(getCurrentUser().getUsername(), newPassword, oldPassword);
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
    public void changeEmail(String username, String password) {
        if (!Validators.getEmailValidator().validate(username)) {
            throw new InvalidFormatException("Invalid email format");
        }
        if (userRepository.findAllByUsername(username).size() == 1) {
            throw new ResourceExistsException("Email exists");
        }
        Optional.ofNullable(getCurrentUser()).ifPresent(user -> {
            if (!checkCredentials(user.getUsername(), password)) {
                throw new IncorrectPasswordException("Invalid password");
            }
            user.setUsername(username);
            userRepository.save(user);
            SecurityContextHolder.clearContext();
        });
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
        if (!Validators.getEmailValidator().validate(username) ||
                !Validators.getPasswordValidator().validate(password)) {
            throw new InvalidFormatException("Bad email or password");
        }
        if (checkIfUserExists(username)) {
            throw new ResourceExistsException("User already exists: " + username);
        }
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
        log.info("New user has been creates {}", username);
    }

    @Override
    public Optional<User> getById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getByProfile(IProfile profile) {
        if (profile == null) {
            return Optional.empty();
        }
        return getById(profile.getId());
    }

    @Override
    public void setAuthorities(Collection<UserGrantedAuthority> authorities) {
        var user = getCurrentUser();
        user.setAuthorities(new HashSet<>(authorities));
        userRepository.save(user);
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
