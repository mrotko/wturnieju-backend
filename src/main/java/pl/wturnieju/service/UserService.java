package pl.wturnieju.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.wturnieju.model.User;
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
    public User resetPassword(String password) {
        //        SecurityContextHolder.getContext().getAuthentication().getName()
        return null;
    }
}
