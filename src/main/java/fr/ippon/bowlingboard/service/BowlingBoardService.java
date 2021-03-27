package fr.ippon.bowlingboard.service;

import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.service.dto.BowlingBoardDTO;

public interface BowlingBoardService {
    BowlingBoardDTO getBowlingBoard(Game game);
}
