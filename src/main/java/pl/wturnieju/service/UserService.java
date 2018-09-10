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
public class UserService implements IUserService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Override
    public User create(User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        existing.ifPresent(it -> {
            throw new IllegalArgumentException("User already exists: " + it.getUsername());
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("New user has been creates {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User resetPassword(String username, String password) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    @Override
    public User resetPassword(String password) {

        return null;
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

    public Optional<GenericProfile> getCurrentUserProfile(ProfileType profileType) {
        return userRepository.findByUsername(getCurrentUser().getUsername())
                .map(User::getProfiles)
                .orElse(new ArrayList<>())
                .stream()
                .filter(genericProfile -> genericProfile.getProfileType() == profileType)
                .findFirst();
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
