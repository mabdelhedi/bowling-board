package fr.ippon.bowlingboard.service;

import fr.ippon.bowlingboard.BowlingBoardApp;
import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.domain.Player;
import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.service.dto.BowlingBoardDTO;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BowlingBoardApp.class)
public class BowlingBoardServiceIT {

    @Autowired
    private BowlingBoardService bowlingBoardService;

    @MockBean
    private ScoreCalculatorService scoreCalculatorService;


    @Test
    void given_noPlayers_when_getBowlingBoard_then_bownlingBoardWithEmptyPlayerScore() {
        Game game = new Game();
        game.setName("Bowling Board Test");

        BowlingBoardDTO bowlingBoardDTO = bowlingBoardService.getBowlingBoard(game);

        assertThat(bowlingBoardDTO).isNotNull();
        assertThat(bowlingBoardDTO.getGame()).isEqualTo(game);
        assertThat(bowlingBoardDTO.getPlayers()).isNullOrEmpty();
    }

    @Test
    void given_playersScores_when_getBowlingBoard_then_bownlingBoardWithCalculatedPlayerScore() {
        Set<Score> scoresForJ1 = generateScoresForPlayer("J1", 1, 5);
        Set<Score> scoresForJ2 = generateScoresForPlayer("J2", 2, 5);
        Set<Score> scoresForJ3 = generateScoresForPlayer("J3", 3, 5);
        Set<Score> scoresForJ4 = generateScoresForPlayer("J4", 4, 5);
        Set<Score> allGameScores = new HashSet<>(scoresForJ1);
        allGameScores.addAll(scoresForJ2);
        allGameScores.addAll(scoresForJ3);
        allGameScores.addAll(scoresForJ4);

        Game game = new Game();
        game.setName("Bowling Board Test");
        game.setScores(allGameScores);

        int totalScoreJ1 = new Random().nextInt(50);
        int totalScoreJ2 = new Random().nextInt(50);
        int totalScoreJ3 = new Random().nextInt(50);
        int totalScoreJ4 = new Random().nextInt(50);

        when(scoreCalculatorService.calculateScore(scoresForJ1)).thenReturn(totalScoreJ1);
        when(scoreCalculatorService.calculateScore(scoresForJ2)).thenReturn(totalScoreJ2);
        when(scoreCalculatorService.calculateScore(scoresForJ3)).thenReturn(totalScoreJ3);
        when(scoreCalculatorService.calculateScore(scoresForJ4)).thenReturn(totalScoreJ4);

        BowlingBoardDTO bowlingBoardDTO = bowlingBoardService.getBowlingBoard(game);

        verify(scoreCalculatorService, times(4)).calculateScore(any());
        assertThat(bowlingBoardDTO).isNotNull();
        assertThat(bowlingBoardDTO.getGame()).isEqualTo(game);
        assertThat(bowlingBoardDTO.getPlayers()).hasSize(4);
        assertThat(bowlingBoardDTO.getPlayers())
            .extracting("player.id", "player.name")
            .containsExactlyInAnyOrder(Tuple.tuple(1L, "J1"), Tuple.tuple(2L, "J2"), Tuple.tuple(3L, "J3"), Tuple.tuple(4L, "J4"));

        assertThat(bowlingBoardDTO.getPlayers()).extracting("currentScore", "scores")
            .containsExactlyInAnyOrder(Tuple.tuple(totalScoreJ1, scoresForJ1),
                Tuple.tuple(totalScoreJ2, scoresForJ2),
                Tuple.tuple(totalScoreJ3, scoresForJ3),
                Tuple.tuple(totalScoreJ4, scoresForJ4));
    }


    private Set<Score> generateScoresForPlayer(String playerName, long playerNum, int nbTour) {
        final Player player = new Player().name(playerName);
        player.setId(playerNum);
        return IntStream.range(0, nbTour)
            .mapToObj(value -> {
                int nbKeels = new Random().nextInt(10);
                Set<Score> scores = new HashSet<>();

                Long firstScoreId = (value * 2 + 1) + 10 * ( playerNum - 1);
                Score firstScore = new Score()
                    .tour(value + 1)
                    .lancier(1)
                    .nbKeel(new Random().nextInt(10))
                    .player(player);
                firstScore.setId(firstScoreId);
                scores.add(firstScore);

                if (nbKeels < 10) {
                    Long secondScoreId = (value * 2 + 2) + 10 * ( playerNum - 1);
                    Score secondScore = new Score()
                        .tour(value + 1)
                        .lancier(2)
                        .nbKeel(new Random().nextInt(10 - nbKeels))
                        .player(player);
                    secondScore.setId(secondScoreId);
                    scores.add(secondScore);
                }

                return scores;
            })
            .reduce((scores, scores2) -> {
                scores.addAll(scores2);
                return scores;
            }).get();
    }

}
