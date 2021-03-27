package fr.ippon.bowlingboard.service.dto;

import fr.ippon.bowlingboard.domain.Player;
import fr.ippon.bowlingboard.domain.Score;

import java.util.Set;

/**
 * A DTO representing a player's current score for a specific game.
 */
public class PlayerScoreDTO {
    private Player player;
    private Set<Score> scores;
    private Integer currentScore;

    public PlayerScoreDTO() {
    }

    public PlayerScoreDTO(Player player, Set<Score> scores, Integer currentScore) {
        this.player = player;
        this.scores = scores;
        this.currentScore = currentScore;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Integer getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }
}
