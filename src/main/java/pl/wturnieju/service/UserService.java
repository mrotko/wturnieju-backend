package pl.wturnieju.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.wturnieju.model.ProfileType;
import pl.wturnieju.model.User;
import pl.wturnieju.model.generic.GenericProfile;
import pl.wturnieju.repository.UserRepository;

@Service
@Log4j2
@AllArgsConstructor
public class UserService implements IUserService, ICurrentUser {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Override
    public void create(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        existing.ifPresent(it -> {
            throw new IllegalArgumentException("User already exists: " + it.getUsername());
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("New user has been creates {}", user.getUsername());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void resetPassword(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String password) {
        resetPassword(getCurrentUser().getUsername(), password);
    }

    @Override
    public boolean check(String email, String password) {
        log.info("Checking user: {}", email);
        Optional<User> user = userRepository.findByUsername(email);
        if (!user.isPresent()) {
            return false;
        }
        return passwordEncoder.matches(password, user.get().getPassword());
    }

    @Override
    public void changeEmail(String email) {
        if (userRepository.findAllByUsername(email).size() == 1) {
            throw new IllegalArgumentException("Email exists");
        }
        var user = getCurrentUser();
        user.setUsername(email);
        userRepository.save(user);
        SecurityContextHolder.clearContext();
    }

    public Optional<GenericProfile> getCurrentUserProfile(ProfileType profileType) {
        return userRepository.findByUsername(getCurrentUser().getUsername())
                .map(User::getProfiles)
                .orElse(new ArrayList<>())
                .stream()
                .filter(genericProfile -> genericProfile.getProfileType() == profileType)
                .findFirst();
    }

    @Override
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElse(null);
    }
}
