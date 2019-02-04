package pl.wturnieju.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.utils.DateUtils;

@RequestMapping("/schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final IGameFixtureService gameFixtureService;

    private final DtoMappers mappers;

    @GetMapping(value = "/public", params = {"dateFrom", "dateTo"})
    public List<ScheduleDto> getPublicSchedule(@RequestParam("dateFrom") String dateFromYYYYMMDD,
            @RequestParam("dateTo") String dateToYYYYMMDD) {
        List<GameFixture> games = gameFixtureService.getAllPublicStartsBetweenDates(
                DateUtils.parseYYYYMMDD(dateFromYYYYMMDD),
                DateUtils.getDateWithLastSecOfDay(DateUtils.parseYYYYMMDD(dateToYYYYMMDD)));
        Map<String, List<GameFixture>> gamesGroupedByTournament = games.stream()
                .collect(Collectors.groupingBy(GameFixture::getTournamentId, Collectors.toList()));

        return gamesGroupedByTournament.entrySet().stream()
                .map(entry -> mappers.createScheduleDto(entry.getKey(), null, entry.getValue()))
                .collect(Collectors.toList());
    }
}
