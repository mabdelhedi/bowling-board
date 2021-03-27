package fr.ippon.bowlingboard.service;

import fr.ippon.bowlingboard.domain.Score;

import java.util.Set;

public interface ScoreCalculatorService {
    Integer calculateScore(Set<Score> scores);
}
