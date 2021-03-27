package fr.ippon.bowlingboard.repository;

import fr.ippon.bowlingboard.domain.Player;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
