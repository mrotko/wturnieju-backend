package pl.wturnieju.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.controller.dto.tournament.schedule.ScheduleDto;
import pl.wturnieju.service.ITournamentScheduleService;
import pl.wturnieju.utils.DateUtils;

@RequestMapping("/schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ITournamentScheduleService scheduleService;

    private final DtoMappers mappers;

    @GetMapping(value = "/{tournamentsIds}", params = {"dateFrom", "dateTo"})
    public List<ScheduleDto> getTournamentsSchedule(@PathVariable("tournamentsIds") List<String> tournamentsIds,
            @RequestParam("dateFrom") String dateFromYYYYMMDD, @RequestParam("dateTo") String dateToYYYYMMDD) {

        var tournamentIdToGamesMapping = tournamentsIds.stream()
                .collect(Collectors.toMap(Function.identity(),
                        id -> scheduleService.getGameFixturesBetweenDates(id,
                                DateUtils.parseYYYYMMDD(dateFromYYYYMMDD),
                                DateUtils.getDateWithLastSecOfDay(DateUtils.parseYYYYMMDD(dateToYYYYMMDD)))));
        return tournamentIdToGamesMapping.entrySet().stream()
                .map(entry -> mappers.createScheduleDto(entry.getKey(), null, entry.getValue()))
                .collect(Collectors.toList());
    }
}
