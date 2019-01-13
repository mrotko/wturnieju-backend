package pl.wturnieju.handler;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentSystemType;

@Data
@Deprecated
public class ScoringConfig {

    private static TournamentSystemConfig config;


    public static double getPoints(TournamentSystemType systemType,
            CompetitionType competitionType,
            ResultType resultType) {
        if (config == null) {
            init();
        }

        return config.getCompetitionConfig()
                .get(systemType).getResultConfig()
                .get(competitionType).getScoring()
                .get(resultType);
    }


    private static void init() {
        config = TournamentSystemConfig.builder()
                .put(TournamentSystemType.SWISS, CompetitionConfig.builder()
                        .put(CompetitionType.CHESS, ResultConfig.builder()
                                .put(ResultType.WIN, 1)
                                .put(ResultType.DRAW, 0.5)
                                .put(ResultType.LOSE, 0)
                                .build())
                        .build())
                .put(TournamentSystemType.ROUND_ROBIN, CompetitionConfig.builder()
                        .put(CompetitionType.CHESS, ResultConfig.builder()
                                .put(ResultType.WIN, 1)
                                .put(ResultType.DRAW, 0.5)
                                .put(ResultType.LOSE, 0)
                                .build())
                        .build())
                .build();
    }


    @Data
    public static class TournamentSystemConfig {
        private Map<TournamentSystemType, CompetitionConfig> competitionConfig = new EnumMap<>(
                TournamentSystemType.class);

        public static TournamentSystemConfigBuilder builder() {
            return new TournamentSystemConfigBuilder();
        }

        public static class TournamentSystemConfigBuilder {
            private Map<TournamentSystemType, CompetitionConfig> config = new EnumMap<>(TournamentSystemType.class);

            public TournamentSystemConfigBuilder put(TournamentSystemType type, CompetitionConfig competitionConfig) {
                config.put(type, competitionConfig);
                return this;
            }

            public TournamentSystemConfig build() {
                var tournamentConfig = new TournamentSystemConfig();
                tournamentConfig.setCompetitionConfig(config);
                return tournamentConfig;
            }
        }
    }

    @Data
    private static class CompetitionConfig {
        private Map<CompetitionType, ResultConfig> resultConfig = new EnumMap<>(CompetitionType.class);

        public static CompetitionConfigBuilder builder() {
            return new CompetitionConfigBuilder();
        }

        public static class CompetitionConfigBuilder {
            private Map<CompetitionType, ResultConfig> config = new EnumMap<>(CompetitionType.class);

            public CompetitionConfigBuilder put(CompetitionType type, ResultConfig resultConfig) {
                config.put(type, resultConfig);
                return this;
            }

            public CompetitionConfig build() {
                var resultConfig = new CompetitionConfig();
                resultConfig.setResultConfig(config);
                return resultConfig;
            }
        }
    }

    @Data
    private static class ResultConfig {
        private Map<ResultType, Double> scoring = new HashMap<>();


        public static ResultConfigBuilder builder() {
            return new ResultConfigBuilder();
        }

        public static class ResultConfigBuilder {
            private Map<ResultType, Double> config = new EnumMap<>(ResultType.class);

            public ResultConfigBuilder put(ResultType type, double points) {
                config.put(type, points);
                return this;
            }

            public ResultConfig build() {
                var resultConfig = new ResultConfig();
                resultConfig.setScoring(config);
                return resultConfig;
            }
        }
    }
}
