package fr.ippon.bowlingboard.repository;

import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.domain.Score;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Score entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findScoresByGame(Game game);
}
