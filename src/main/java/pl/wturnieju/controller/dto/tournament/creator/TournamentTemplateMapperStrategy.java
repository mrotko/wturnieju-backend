package pl.wturnieju.controller.dto.tournament.creator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.exception.UnknownEnumTypeException;
import pl.wturnieju.tournament.Tournament;

@Component
@RequiredArgsConstructor
public class TournamentTemplateMapperStrategy {

    private final ChessTournamentTemplateMapper chessTournamentTemplateMapper;

    private final CustomTournamentTemplateMapper customTournamentTemplateMapper;

    private final FootballTournamentTemplateMapper footballTournamentTemplateMapper;

    private final TennisTournamentTemplateMapper tennisTournamentTemplateMapper;

    public Tournament mapToTournament(TournamentTemplateDto dto) {

        switch (dto.getCompetitionType()) {
        case CHESS:
            return chessTournamentTemplateMapper.mapToChessTournament((ChessTournamentTemplateDto) dto);
        case FOOTBALL:
            return customTournamentTemplateMapper.mapToCustomTournament((CustomTournamentTemplateDto) dto);
        case TENNIS:
            return tennisTournamentTemplateMapper.mapToTennisTournament((TennisTournamentTemplateDto) dto);
        case CUSTOM:
            return customTournamentTemplateMapper.mapToCustomTournament((CustomTournamentTemplateDto) dto);
        default:
            throw new UnknownEnumTypeException(dto.getCompetitionType());
        }
    }
}
