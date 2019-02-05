package pl.wturnieju.search;

import java.util.List;
import java.util.StringJoiner;
import java.util.StringTokenizer;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.repository.TournamentRepository;
import pl.wturnieju.tournament.Tournament;

@RequiredArgsConstructor
@Service
public class TournamentSimpleSearchService implements ISearch<TournamentSearchCriteria, Tournament> {

    private final TournamentRepository tournamentRepository;

    @Override
    public List<Tournament> find(TournamentSearchCriteria criteria) {
        var page = PageRequest.of(0, criteria.getLimit());

        return tournamentRepository.findAllPublicByNameWithRegex(
                createRegexExpMatchingAllTokens(criteria.getText()), page);
    }

    private String createRegexExpMatchingAllTokens(String text) {
        var tokenizer = new StringTokenizer(text);
        var joiner = new StringJoiner("");

        while (tokenizer.hasMoreTokens()) {
            joiner.add("(?=(.*" + tokenizer.nextToken() + ".*))");
        }

        return joiner.toString();
    }
}
