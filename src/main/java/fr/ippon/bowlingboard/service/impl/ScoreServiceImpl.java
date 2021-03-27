package fr.ippon.bowlingboard.service.impl;

import fr.ippon.bowlingboard.domain.Game;
import fr.ippon.bowlingboard.service.ScoreService;
import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.repository.ScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Score}.
 */
@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {

    private final Logger log = LoggerFactory.getLogger(ScoreServiceImpl.class);

    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * Save a score.
     *
     * @param score the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Score save(Score score) {
        log.debug("Request to save Score : {}", score);
        return scoreRepository.save(score);
    }

    /**
     * Get all the scores.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Score> findAll() {
        log.debug("Request to get all Scores");
        return scoreRepository.findAll();
    }

    /**
     * Get one score by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Score> findOne(Long id) {
        log.debug("Request to get Score : {}", id);
        return scoreRepository.findById(id);
    }

    /**
     * Get scores from game.
     *
     * @param game the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Score> findScoresByGame(Game game) {
        log.debug("Request to get Scores from gameId : {}", game.getId());
        return scoreRepository.findScoresByGame(game);
    }


    /**
     * Delete the score by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Score : {}", id);
        scoreRepository.deleteById(id);
    }
}
