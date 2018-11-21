package pl.wturnieju.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.User;
import pl.wturnieju.search.ISearch;

@Service
@RequiredArgsConstructor
public class TournamentDashboardService {

    @Qualifier("UserSimpleSearch")
    private final ISearch<String, User> userSearch;


}
