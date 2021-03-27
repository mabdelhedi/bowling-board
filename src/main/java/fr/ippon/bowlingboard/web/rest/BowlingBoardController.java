package fr.ippon.bowlingboard.web.rest;

import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.service.BowlingBoardService;
import fr.ippon.bowlingboard.service.dto.BowlingBoardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class BowlingBoardController {

    private final BowlingBoardService bowlingBoardService;

    public BowlingBoardController(BowlingBoardService bowlingBoardService) {
        this.bowlingBoardService = bowlingBoardService;
    }

    @PostMapping("/bowling-board")
    public ResponseEntity<BowlingBoardDTO> getBoardFromGame(@RequestBody @Valid Game game) {
        return ResponseEntity.ok(bowlingBoardService.getBowlingBoard(game));
    }
}
