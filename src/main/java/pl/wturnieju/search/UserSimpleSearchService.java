package pl.wturnieju.search;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.User;
import pl.wturnieju.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserSimpleSearchService implements ISearch<String, User> {

    private final UserRepository userRepository;

    @Override
    public List<User> find(String searchInput) {
        List<String> searchInputParams = createInputSearchParams(searchInput);
        return userRepository.findAll().stream()
                .filter(user -> isUserMatch(user, searchInputParams))
                .collect(Collectors.toList());
    }

    private List<String> createUserSearchParams(User user) {
        return Stream.of(user.getUsername().toLowerCase(), user.getName(), user.getSurname())
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private boolean isUserMatch(User user, List<String> searchParams) {
        List<String> userParams = createUserSearchParams(user);

        return searchParams.stream()
                .allMatch(searchParam -> userParams.stream()
                        .anyMatch(userParam -> userParam.contains(searchParam)));
    }

    private List<String> createInputSearchParams(String searchInput) {
        return Stream.of(searchInput.split(" "))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
