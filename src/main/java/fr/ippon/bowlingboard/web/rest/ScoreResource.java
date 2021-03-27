package fr.ippon.bowlingboard.web.rest;

import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.service.ScoreService;
import fr.ippon.bowlingboard.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.ippon.bowlingboard.domain.Score}.
 */
@RestController
@RequestMapping("/api")
public class ScoreResource {

    private final Logger log = LoggerFactory.getLogger(ScoreResource.class);

    private static final String ENTITY_NAME = "score";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScoreService scoreService;

    public ScoreResource(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * {@code POST  /scores} : Create a new score.
     *
     * @param score the score to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new score, or with status {@code 400 (Bad Request)} if the score has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@Valid @RequestBody Score score) throws URISyntaxException {
        log.debug("REST request to save Score : {}", score);
        if (score.getId() != null) {
            throw new BadRequestAlertException("A new score cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Score result = scoreService.save(score);
        return ResponseEntity.created(new URI("/api/scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scores} : Updates an existing score.
     *
     * @param score the score to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated score,
     * or with status {@code 400 (Bad Request)} if the score is not valid,
     * or with status {@code 500 (Internal Server Error)} if the score couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scores")
    public ResponseEntity<Score> updateScore(@Valid @RequestBody Score score) throws URISyntaxException {
        log.debug("REST request to update Score : {}", score);
        if (score.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Score result = scoreService.save(score);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, score.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /scores} : get all the scores.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scores in body.
     */
    @GetMapping("/scores")
    public List<Score> getAllScores() {
        log.debug("REST request to get all Scores");
        return scoreService.findAll();
    }

    /**
     * {@code GET  /scores/:id} : get the "id" score.
     *
     * @param id the id of the score to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the score, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scores/{id}")
    public ResponseEntity<Score> getScore(@PathVariable Long id) {
        log.debug("REST request to get Score : {}", id);
        Optional<Score> score = scoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(score);
    }

    /**
     * {@code DELETE  /scores/:id} : delete the "id" score.
     *
     * @param id the id of the score to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scores/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        log.debug("REST request to delete Score : {}", id);
        scoreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
