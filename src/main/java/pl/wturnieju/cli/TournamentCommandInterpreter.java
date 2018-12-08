package pl.wturnieju.cli;

import pl.wturnieju.cli.dto.TournamentInfoResponse;
import pl.wturnieju.service.ITournamentService;

public class TournamentCommandInterpreter extends CommandInterpreter<TournamentInfoResponse> {

    private final ITournamentService tournamentService;

    public TournamentCommandInterpreter(ITournamentService tournamentService,
            ICommandParsedDataProvider parsedDataProvider) {
        super(parsedDataProvider);
        this.tournamentService = tournamentService;
    }

    @Override
    TournamentInfoResponse getResponse() {
        var response = new TournamentInfoResponse();

        getParameterValue("id", "i").ifPresent(tournamentId -> {
            tournamentService.getById(tournamentId).ifPresent(tournament -> {
                response.setStatus(tournament.getStatus().name());
                response.setTournamentId(tournamentId);
                response.setStartDate(tournament.getStartDate());
                response.setEndDate(tournament.getEndDate());
                response.setTournamentName(tournament.getName());
                response.setCompetitionName(tournament.getCompetitionType().name());
                response.setSystemName(tournament.getSystemType().name());
            });
        });
        return response;
    }
}
