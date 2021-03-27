package fr.ippon.bowlingboard.service.dto;

import fr.ippon.bowlingboard.domain.Game;

import java.util.Set;

/**
 * A DTO representing a bowling board for a specific game.
 */
public class BowlingBoardDTO {
    private Game game;
    private Set<PlayerScoreDTO> players;

    public BowlingBoardDTO() {
    }

    public BowlingBoardDTO(Game game, Set<PlayerScoreDTO> players) {
        this.game = game;
        this.players = players;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<PlayerScoreDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerScoreDTO> players) {
        this.players = players;
    }
}
