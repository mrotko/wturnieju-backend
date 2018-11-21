package pl.wturnieju.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearch<String, User> userSearch;

    @GetMapping("/user")
    public List<User> findUsers(@RequestParam("q") String query,
            @RequestParam("limit") @Nullable Integer limit,
            @RequestParam("excluded") @Nullable List<String> excluded) {

        Stream<User> userStream = userSearch.find(query).stream();

        if (excluded != null) {
            userStream = userStream.filter(user -> !excluded.contains(user.getId()));
        }
        if (limit != null) {
            userStream = userStream.limit(limit);
        }
        return userStream.collect(Collectors.toList());
    }
}
