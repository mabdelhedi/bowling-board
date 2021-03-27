package fr.ippon.bowlingboard.service.impl;

import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.domain.Player;
import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.service.BowlingBoardService;
import fr.ippon.bowlingboard.service.ScoreCalculatorService;
import fr.ippon.bowlingboard.service.ScoreService;
import fr.ippon.bowlingboard.service.dto.BowlingBoardDTO;
import fr.ippon.bowlingboard.service.dto.PlayerScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class BowlingBoardServiceImpl implements BowlingBoardService {

    @Autowired
    private ScoreCalculatorService scoreCalculatorService;

    @Autowired
    private ScoreService scoreService;

    @Override
    public BowlingBoardDTO getBowlingBoard(Game game) {
        List<Score> scoresForGame = scoreService.findScoresByGame(game);
        Map<Player, List<Score>> scoresByPlayer = scoresForGame.stream()
            .collect(groupingBy(Score::getPlayer));

        Set<PlayerScoreDTO> playerScoreDTOS = scoresByPlayer.entrySet().stream()
            .map(scoresForPlayer -> {
                Player player = scoresForPlayer.getKey();
                Set<Score> playerScores = scoresForPlayer.getValue().stream()
                    .sorted(Comparator.comparing(Score::getTour).thenComparing(Score::getLancier))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
                int scoreTotal = scoreCalculatorService.calculateScore(playerScores);
                return new PlayerScoreDTO(player, playerScores, scoreTotal);
            })
            .collect(Collectors.toSet());

        return new BowlingBoardDTO(game, playerScoreDTOS);
    }
}
