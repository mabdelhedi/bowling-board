package fr.ippon.bowlingboard.service.impl;

import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.service.ScoreCalculatorService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ScoreCalculatorServiceImpl implements ScoreCalculatorService {

    private static final BiPredicate<Integer, Integer> isStrike = (firstLancier, secondLancier) -> firstLancier == 10;
    private static final BiPredicate<Integer, Integer> isSpare = (firstLancier, secondLancier) -> firstLancier + secondLancier == 10 && firstLancier < 10;

    @Override
    public Integer calculateScore(Set<Score> scores) {
        Map<Integer, List<Score>> scoreMap = scores.stream()
            .sorted(Comparator.comparing(Score::getTour).thenComparing(Score::getLancier))
            .collect(groupingBy(Score::getTour));

        if (scoreMap.keySet().equals(IntStream.rangeClosed(1, scoreMap.size()).boxed().collect(Collectors.toSet()))) {
            if (!scoreMap.values().stream().allMatch(scoresForTour -> scoresForTour.stream()
                .mapToInt(Score::getLancier)
                .boxed()
                .collect(Collectors.toList())
                .equals(IntStream.rangeClosed(1, scoresForTour.size()))
            )) {
                //TODO throw exception
            }
        } else {
            //TODO throw exception
        }


        return scoreMap.entrySet().stream()
            .mapToInt(tourScores -> {
                int numTour = tourScores.getKey();
                int scoreLancier1 = tourScores.getValue().get(0).getNbKeel();
                int scoreLancier2 = tourScores.getValue().size() > 1 ? tourScores.getValue().get(1).getNbKeel() : 0;
                int totalTourScore = scoreLancier1 + scoreLancier2;
                if (isStrike.test(scoreLancier1, scoreLancier2)) {
                    totalTourScore += calculateStrikeScoreToAdd(scoreMap, numTour);
                } else if (isSpare.test(scoreLancier1, scoreLancier2)) {
                    totalTourScore += calculateSpareScoreToAdd(scoreMap, numTour);
                }
                return totalTourScore;
            })
            .sum();
    }


    private int calculateSpareScoreToAdd(Map<Integer, List<Score>> scoreMap, int numTour) {
        int scoreToAdd = 0;
        if (scoreMap.get(numTour).size() == 3) {
            scoreToAdd = scoreMap.get(numTour).get(2).getNbKeel();
        } else {
            List<Score> nextScores = scoreMap.get(numTour + 1);
            if (!Collections.isEmpty(nextScores)) {
                Score nextScore1 = nextScores.get(0);
                scoreToAdd = nextScore1.getNbKeel();
            }
        }
        return scoreToAdd;
    }

    private int calculateStrikeScoreToAdd(Map<Integer, List<Score>> scoreMap, int numTour) {
        int scoreToAdd = 0;
        if (scoreMap.get(numTour).size() == 3) {
            scoreToAdd = scoreMap.get(numTour).get(1).getNbKeel() + scoreMap.get(numTour).get(2).getNbKeel();
        } else {
            List<Score> nextScores = scoreMap.get(numTour + 1);
            if (!Collections.isEmpty(nextScores)) {
                Score nextScore1 = nextScores.get(0);
                scoreToAdd = nextScore1.getNbKeel();
                if (scoreToAdd == 10) {
                    List<Score> nextScores2 = scoreMap.get(numTour + 2);
                    scoreToAdd += Optional.ofNullable(nextScores2)
                        .map(scores1 -> scores1.get(0).getNbKeel())
                        .orElse(0);
                } else if (nextScores.size() > 1) {
                    scoreToAdd += nextScores.get(1).getNbKeel();
                }
            }
        }
        return scoreToAdd;
    }

}
