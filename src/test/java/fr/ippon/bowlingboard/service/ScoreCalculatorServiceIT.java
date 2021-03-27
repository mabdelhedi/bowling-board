package fr.ippon.bowlingboard.service;

import fr.ippon.bowlingboard.BowlingBoardApp;
import fr.ippon.bowlingboard.domain.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BowlingBoardApp.class)
class ScoreCalculatorServiceIT {

    @Autowired
    private ScoreCalculatorService scoreCalculatorService;

    @Test
    void given_emptyScores_when_calculateScore_then_scoreIsZero() {
        Integer result = scoreCalculatorService.calculateScore(new HashSet<>());

        assertThat(result).isEqualTo(0);
    }

    @Test
    void given_scores_when_calculateScore_then_sumKeels() {

        Score scoreTour1Lancier1 = new Score().tour(1).lancier(1).nbKeel(5);
        scoreTour1Lancier1.setId(1L);
        Score scoreTour1Lancier2 = new Score().tour(1).lancier(2).nbKeel(2);
        scoreTour1Lancier2.setId(2L);
        Score scoreTour2Lancier1 = new Score().tour(2).lancier(1).nbKeel(3);
        scoreTour2Lancier1.setId(3L);
        Score scoreTour2Lancier2 = new Score().tour(2).lancier(2).nbKeel(5);
        scoreTour2Lancier2.setId(4L);
        Score scoreTour3Lancier1 = new Score().tour(3).lancier(1).nbKeel(4);
        scoreTour3Lancier1.setId(5L);
        Score scoreTour3Lancier2 = new Score().tour(3).lancier(2).nbKeel(4);
        scoreTour3Lancier2.setId(6L);

        Set<Score> scores = Set.of(scoreTour1Lancier1,
            scoreTour1Lancier2,
            scoreTour2Lancier1,
            scoreTour2Lancier2,
            scoreTour3Lancier1,
            scoreTour3Lancier2);

        Integer expectedScore = 23;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }

    @Test
    void given_consecutiveStrikes_when_calculateScore_then_sumKeels() {

        Score scoreTour1Lancier1 = new Score().tour(1).lancier(1).nbKeel(5);
        scoreTour1Lancier1.setId(1L);
        Score scoreTour1Lancier2 = new Score().tour(1).lancier(2).nbKeel(2);
        scoreTour1Lancier2.setId(2L);
        Score scoreTour2Lancier1 = new Score().tour(2).lancier(1).nbKeel(10);
        scoreTour2Lancier1.setId(3L);
        Score scoreTour3Lancier1 = new Score().tour(3).lancier(1).nbKeel(10);
        scoreTour3Lancier1.setId(4L);
        Score scoreTour4Lancier1 = new Score().tour(4).lancier(1).nbKeel(4);
        scoreTour4Lancier1.setId(5L);
        Score scoreTour4Lancier2 = new Score().tour(4).lancier(2).nbKeel(4);
        scoreTour4Lancier2.setId(6L);

        Set<Score> scores = Set.of(scoreTour1Lancier1,
            scoreTour1Lancier2,
            scoreTour2Lancier1,
            scoreTour3Lancier1,
            scoreTour4Lancier1,
            scoreTour4Lancier2);

        Integer expectedScore = 57;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }


    @Test
    void given_scoresWithStrike_when_calculateScore_then_twoLancierAfterAddedToPreviousScore() {

        Score scoreTour1Lancier1 = new Score().tour(1).lancier(1).nbKeel(10);
        scoreTour1Lancier1.setId(1L);
        Score scoreTour2Lancier1 = new Score().tour(2).lancier(1).nbKeel(4);
        scoreTour2Lancier1.setId(2L);
        Score scoreTour2Lancier2 = new Score().tour(2).lancier(2).nbKeel(3);
        scoreTour2Lancier2.setId(3L);
        Score scoreTour3Lancier1 = new Score().tour(3).lancier(1).nbKeel(5);
        scoreTour3Lancier1.setId(4L);
        Score scoreTour3Lancier2 = new Score().tour(3).lancier(2).nbKeel(4);
        scoreTour3Lancier2.setId(5L);

        Set<Score> scores = Set.of(scoreTour2Lancier1,
            scoreTour1Lancier1,
            scoreTour2Lancier2,
            scoreTour3Lancier1,
            scoreTour3Lancier2);

        Integer expectedScore = 33;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }


    @Test
    void given_scoresWithSpares_when_calculateScore_then_nextLancierAfterAddedToPreviousScore() {

        Score scoreTour1Lancier1 = new Score().tour(1).lancier(1).nbKeel(8);
        scoreTour1Lancier1.setId(1L);
        Score scoreTour1Lancier2 = new Score().tour(1).lancier(2).nbKeel(2);
        scoreTour1Lancier2.setId(2L);
        Score scoreTour2Lancier1 = new Score().tour(2).lancier(1).nbKeel(3);
        scoreTour2Lancier1.setId(3L);
        Score scoreTour2Lancier2 = new Score().tour(2).lancier(2).nbKeel(5);
        scoreTour2Lancier2.setId(4L);
        Score scoreTour3Lancier1 = new Score().tour(3).lancier(1).nbKeel(4);
        scoreTour3Lancier1.setId(5L);
        Score scoreTour3Lancier2 = new Score().tour(3).lancier(2).nbKeel(4);
        scoreTour3Lancier2.setId(6L);

        Set<Score> scores = Set.of(scoreTour1Lancier1,
            scoreTour1Lancier2,
            scoreTour2Lancier1,
            scoreTour2Lancier2,
            scoreTour3Lancier1,
            scoreTour3Lancier2);

        Integer expectedScore = 29;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }

    @Test
    void given_lastTourIsStrike_when_calculateScore_then_twoAdditionalLancierAfterAddedToPreviousScore() {

        Set<Score> scores = new HashSet<>();
        IntStream.range(0, 9)
            .forEach(value -> {
                Score scoreLancier1 = new Score().tour(value + 1).lancier(1).nbKeel(4);
                scoreLancier1.setId((long) value * 2 + 1);

                Score scoreLancier2 = new Score().tour(value + 1).lancier(2).nbKeel(3);
                scoreLancier2.setId((long) value * 2 + 2);
                scores.add(scoreLancier1);
                scores.add(scoreLancier2);
            });

        Score scoreLancier1 = new Score().tour(10).lancier(1).nbKeel(10);
        scoreLancier1.setId(19L);

        Score scoreLancier2 = new Score().tour(10).lancier(2).nbKeel(3);
        scoreLancier2.setId(20L);

        Score scoreLancier3 = new Score().tour(10).lancier(3).nbKeel(3);
        scoreLancier3.setId(21L);

        scores.add(scoreLancier1);
        scores.add(scoreLancier2);
        scores.add(scoreLancier3);

        Integer expectedScore = 82;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }

    @Test
    void given_lastTourIsSpare_when_calculateScore_then_oneLancierAfterAddedToPreviousScore() {

        Set<Score> scores = new HashSet<>();
        IntStream.range(0, 9)
            .forEach(value -> {
                Score scoreLancier1 = new Score().tour(value + 1).lancier(1).nbKeel(4);
                scoreLancier1.setId((long) value * 2 + 1);

                Score scoreLancier2 = new Score().tour(value + 1).lancier(2).nbKeel(3);
                scoreLancier2.setId((long) value * 2 + 2);
                scores.add(scoreLancier1);
                scores.add(scoreLancier2);
            });

        Score scoreLancier1 = new Score().tour(10).lancier(1).nbKeel(8);
        scoreLancier1.setId(19L);

        Score scoreLancier2 = new Score().tour(10).lancier(2).nbKeel(2);
        scoreLancier2.setId(20L);

        Score scoreLancier3 = new Score().tour(10).lancier(3).nbKeel(3);
        scoreLancier3.setId(21L);

        scores.add(scoreLancier1);
        scores.add(scoreLancier2);
        scores.add(scoreLancier3);

        Integer expectedScore = 76;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }


    @Test
    void given_onlyStrikes_when_calculateScore_then_scoreIsThreeHundred() {

        Set<Score> scores = new HashSet<>();
        IntStream.range(1, 10)
            .forEach(value -> {
                Score scoreLancier1 = new Score().tour(value).lancier(1).nbKeel(10);
                scoreLancier1.setId((long) value);

                scores.add(scoreLancier1);
            });

        Score scoreLancier1 = new Score().tour(10).lancier(1).nbKeel(10);
        scoreLancier1.setId(10L);

        Score scoreLancier2 = new Score().tour(10).lancier(2).nbKeel(10);
        scoreLancier2.setId(11L);

        Score scoreLancier3 = new Score().tour(10).lancier(3).nbKeel(10);
        scoreLancier3.setId(12L);

        scores.add(scoreLancier1);
        scores.add(scoreLancier2);
        scores.add(scoreLancier3);

        Integer expectedScore = 300;

        Integer result = scoreCalculatorService.calculateScore(scores);

        assertThat(result).isEqualTo(expectedScore);
    }


}
