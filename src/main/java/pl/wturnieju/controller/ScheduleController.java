package pl.wturnieju.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.service.IGameFixtureService;

@RequestMapping("/schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final IGameFixtureService gameFixtureService;

    private final DtoMappers mappers;

    @GetMapping(value = "/public", params = {"beginDayDateTime"})
    public List<ScheduleDto> getPublicScheduleFromDay(@RequestParam("beginDayDateTime") Timestamp beginDayDateTime) {
        var games = gameFixtureService.getAllPublicStartsBetweenDates(beginDayDateTime, beginDayDateTime.plusDays(1));
        var gamesGroupedByTournamentId = games.stream()
                .collect(Collectors.groupingBy(GameFixture::getTournamentId, Collectors.toList()));

        return gamesGroupedByTournamentId.entrySet().stream()
                .map(entry -> mappers.createScheduleDto(entry.getKey(), null, entry.getValue()))
                .collect(Collectors.toList());
    }
}
