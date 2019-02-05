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
import pl.wturnieju.controller.dto.search.SearchResultDto;
import pl.wturnieju.controller.dto.search.TournamentSearchDto;
import pl.wturnieju.controller.dto.search.TournamentSearchDtoMapper;
import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;
import pl.wturnieju.search.TournamentSearchCriteria;
import pl.wturnieju.tournament.Tournament;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearch<String, User> userSearch;

    private final ISearch<TournamentSearchCriteria, Tournament> tournamentSearch;

    private final TournamentSearchDtoMapper tournamentSearchDtoMapper;

    @GetMapping("/user")
    public List<User> findUsers(@RequestParam("q") String query,
            @RequestParam(value = "limit", defaultValue = "20", required = false) @Nullable Integer limit,
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

    @GetMapping(value = "/tournament", params = {"q"})
    public SearchResultDto<TournamentSearchDto> findTournaments(
            @RequestParam("q") String query,
            @RequestParam(value = "limit", defaultValue = "20", required = false) Integer limit) {
        var criteria = new TournamentSearchCriteria();
        criteria.setText(query);
        criteria.setLimit(limit);
        var tournaments = tournamentSearch.find(criteria);

        SearchResultDto<TournamentSearchDto> searchResultDto = new SearchResultDto<>();
        searchResultDto.setData(tournamentSearchDtoMapper.mapToTournamentSearchDtos(tournaments));
        return searchResultDto;
    }
}
